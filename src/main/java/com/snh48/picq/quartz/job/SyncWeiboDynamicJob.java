package com.snh48.picq.quartz.job;

import java.util.List;
import java.util.Optional;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.transaction.annotation.Transactional;

import com.snh48.picq.entity.QQCommunity;
import com.snh48.picq.entity.weibo.Dynamic;
import com.snh48.picq.entity.weibo.WeiboUser;
import com.snh48.picq.https.HttpsURL;
import com.snh48.picq.https.WeiboTool;
import com.snh48.picq.kuq.KuqManage;
import com.snh48.picq.repository.weibo.DynamicRepository;
import com.snh48.picq.repository.weibo.WeiboUserRepository;
import com.snh48.picq.service.DynamicMonitorService;
import com.snh48.picq.vo.DynamicMonitorVO;

import cc.moecraft.icq.PicqBotX;
import cc.moecraft.icq.sender.IcqHttpApi;
import lombok.extern.log4j.Log4j2;

/**
 * 同步微博动态任务
 * 
 * @author shiro
 *
 */
@Log4j2
@Transactional
@DisallowConcurrentExecution // 任务串行注解
public class SyncWeiboDynamicJob extends QuartzJobBean {

	@Autowired
	private WeiboUserRepository weiboUserRepository;

	@Autowired
	private DynamicRepository dynamicRepository;

	@Autowired
	private DynamicMonitorService dynamicMonitorService;

	@Autowired
	private PicqBotX bot;

//	@Autowired
//	private JmsMessagingTemplate jmsMessagingTemplate;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		log.info("--------------开始：SyncWeiboDynamicJob");
		// 获库中保存的取所有的微博用户
		List<WeiboUser> userList = weiboUserRepository.findAll();
		for (WeiboUser weiboUser : userList) {
			
			log.info("同步微博用户动态==>>{}", weiboUser.getUserName());

			try {
				Thread.sleep(HttpsURL.REQUEST_INTERVAL_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			long containerDynamicId = weiboUser.getContainerDynamicId();
			List<Dynamic> dynamiList = WeiboTool.getDynamicList(containerDynamicId);

			for (Dynamic dynamic : dynamiList) {
				Optional<Dynamic> od = dynamicRepository.findById(dynamic.getId());
				if (od.isPresent()) {// 当该条动态存在数据库中时,判断是否置顶
					if (dynamic.getIsTop()) {// 若为置顶博，则结束本次循环
//						log.info("数据库已存在置顶博。[{}]", dynamic.getId());
						continue;
					} else {// 否则结束循环
//						log.info("数据库已存在非置顶博。[{}]", dynamic.getId());
						break;
					}
				}

				// 发送消息
				log.info("发送微博动态 [{}]", dynamic.getId());
				try {
					sendWeiboDynamic(dynamic);
				} catch (InterruptedException e) {
					log.error("发送微博动态 [{}] 到消息队列失败。{}", dynamic.getId(), e.getMessage());
				}

				// 写入数据库
				dynamicRepository.save(dynamic);

				// 发送消息队列
//				try {
//					jmsMessagingTemplate.convertAndSend(new ActiveMQQueue(ActivemqPhysical.QUEUE_SEND_WEIBO_DYNAMIC),
//							RedisUtil.serializeObj(dynamic));
//				} catch (MessagingException | SerializationException e) {
//					log.error("发送微博动态 [{}] 到消息队列失败。{}", dynamic.getId(), e.getMessage());
//				}
			}
		}

		log.info("--------------结束：SyncWeiboDynamicJob");
	}

	/**
	 * 发送微博动态
	 * 
	 * @param dynamic 动态实体
	 * @throws InterruptedException
	 */
	private void sendWeiboDynamic(Dynamic dynamic) throws InterruptedException {
		long userId = dynamic.getUserId();
		List<DynamicMonitorVO> voList = dynamicMonitorService.getCache(userId);
		String message = deserializeMsgContent(dynamic);
		IcqHttpApi icqHttpApi = bot.getAccountManager().getNonAccountSpecifiedApi();

		if (voList == null) {
			return;
		}

		for (DynamicMonitorVO vo : voList) {
			QQCommunity qqCommunity = vo.getQqCommunity();
//			log.info("[{}]的动态发送给 {}({})", dynamic.getCreatedAt(), qqCommunity.getCommunityName(), qqCommunity.getId());
			KuqManage.sendSyncMessage(icqHttpApi, message, qqCommunity);
		}

		// 设置为已发送
		dynamic.setIsSend(2);
	}

	/**
	 * 转化消息内容
	 * 
	 * @param dynamic 微博动态实体
	 * @return 格式化后的动态
	 */
	private String deserializeMsgContent(Dynamic dynamic) {
		StringBuffer sb = new StringBuffer();
		sb.append("来自微博：");
		sb.append(dynamic.getSenderName());
		sb.append(" ");
		sb.append(dynamic.getCreatedAt());
		sb.append("\n");
		sb.append(dynamic.getWeiboContent());
		return sb.toString().replace("<br>", "\n");
	}

}