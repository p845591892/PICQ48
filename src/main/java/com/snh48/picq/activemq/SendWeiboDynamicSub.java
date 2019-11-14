//package com.snh48.picq.activemq;
//
//import java.util.List;
//
//import org.crazycake.shiro.exception.SerializationException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jms.annotation.JmsListener;
//import org.springframework.stereotype.Component;
//
//import com.snh48.picq.entity.QQCommunity;
//import com.snh48.picq.entity.weibo.Dynamic;
//import com.snh48.picq.kuq.KuqManage;
//import com.snh48.picq.repository.weibo.DynamicRepository;
//import com.snh48.picq.service.DynamicMonitorService;
//import com.snh48.picq.utils.RedisUtil;
//import com.snh48.picq.vo.DynamicMonitorVO;
//
//import cc.moecraft.icq.PicqBotX;
//import cc.moecraft.icq.sender.IcqHttpApi;
//import lombok.extern.log4j.Log4j2;
//
///**
// * 发送微博动态的消息队列消费者
// * 
// * @author shiro
// *
// */
//@Log4j2
//@Component
//public class SendWeiboDynamicSub {
//
//	@Autowired
//	private DynamicMonitorService dynamicMonitorService;
//
//	@Autowired
//	private DynamicRepository dynamicRepository;
//
//	@Autowired
//	private PicqBotX bot;
//
//	/**
//	 * Jms监听器
//	 * 
//	 * @param bytes 对象的bytes序列
//	 * @throws InterruptedException
//	 * @throws SerializationException
//	 */
//	@JmsListener(destination = ActivemqPhysical.QUEUE_SEND_WEIBO_DYNAMIC)
//	public void run(byte[] bytes) throws InterruptedException, SerializationException {
//		Dynamic dynamic = (Dynamic) RedisUtil.deserializeObj(bytes);
//		long userId = dynamic.getUserId();
//		List<DynamicMonitorVO> voList = dynamicMonitorService.getCache(userId);
//		String message = deserializeMsgContent(dynamic);
//		IcqHttpApi icqHttpApi = bot.getAccountManager().getNonAccountSpecifiedApi();
//
//		if (voList == null) {
//			return;
//		}
//
//		log.info("[{}]有条微博新动态全量发送。", dynamic.getSenderName());
//
//		for (DynamicMonitorVO vo : voList) {
//			QQCommunity qqCommunity = vo.getQqCommunity();
//			KuqManage.sendSyncMessage(icqHttpApi, message, qqCommunity);
//		}
//
//		// 设置为已发送
//		dynamic.setIsSend(2);
//		dynamicRepository.save(dynamic);
//	}
//
//	/**
//	 * 转化消息内容
//	 * 
//	 * @param dynamic 微博动态实体
//	 * @return 格式化后的动态
//	 */
//	private String deserializeMsgContent(Dynamic dynamic) {
//		StringBuffer sb = new StringBuffer();
//		sb.append("来自微博：");
//		sb.append(dynamic.getSenderName());
//		sb.append(" ");
//		sb.append(dynamic.getCreatedAt());
//		sb.append("\n");
//		sb.append(dynamic.getWeiboContent());
//		return sb.toString().replace("<br>", "\n");
//	}
//
//}