package com.snh48.picq.quartz.job;

import java.util.ArrayList;
import java.util.List;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.transaction.annotation.Transactional;

import com.snh48.picq.core.Common.MsgSend;
import com.snh48.picq.core.Common.SleepMillis;
import com.snh48.picq.entity.QQCommunity;
import com.snh48.picq.entity.snh48.Member;
import com.snh48.picq.entity.snh48.RoomMessage;
import com.snh48.picq.https.Pocket48Tool;
import com.snh48.picq.kuq.KuqManage;
import com.snh48.picq.repository.snh48.MemberRepository;
import com.snh48.picq.repository.snh48.RoomMessageRepository;
import com.snh48.picq.service.MemberService;
import com.snh48.picq.service.RoomMonitorService;
import com.snh48.picq.utils.DateUtil;
import com.snh48.picq.utils.StringUtil;
import com.snh48.picq.vo.RoomMonitorVO;

import cc.moecraft.icq.PicqBotX;
import cc.moecraft.icq.sender.IcqHttpApi;
import lombok.extern.log4j.Log4j2;

/**
 * 同步SNH48成员的口袋房间消息任务
 * 
 * @author shiro
 *
 */
@Log4j2
@Transactional
@DisallowConcurrentExecution // 任务串行注解
public class SyncRoomMessageJob extends QuartzJobBean {

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private RoomMessageRepository roomMessageRepository;

	@Autowired
	private RoomMonitorService roomMonitorService;

	@Autowired
	private MemberService memberService;

	@Autowired
	private PicqBotX bot;

//	@Autowired
//	private JmsMessagingTemplate jmsMessagingTemplate;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		log.info("--------------[开始] 同步口袋48房间消息任务。");
		
		// 获取开启了监控的成员，逐一发送请求获取其口袋房间消息
		List<Member> members = memberRepository.findByRoomMonitor(1);
		for (Member member : members) {

//			log.info("同步口袋房间消息==>>{}", member.getName());

			RoomMessage sourceRoomMessage = roomMessageRepository
					.findFirstByRoomIdOrderByMsgTimeDesc(member.getRoomId());

			long sourceMsgTime = 0; // 原消息时间
			int flag = 1; // while循环的flag
			long nextTime = 0; // https请求参数
			List<RoomMessage> sendRoomMessageList = new ArrayList<RoomMessage>(); // 待发送的房间消息集合

			while (flag == 1) {
				if (sourceRoomMessage == null) { // 原消息不存在，即，当第一次同步时，只进行本次while循环。
					flag = 0; // 设置flag=0，就不会进行下次while循环了。

//					log.info("【{}】口袋房间消息为第一次同步。", member.getName());

				} else { // 原消息存在
					sourceMsgTime = sourceRoomMessage.getMsgTime().getTime();// 赋值原消息时间
					long lineTime = DateUtil.countDayToDate(-1).getTime();

//					log.info("历史消息时间为 {} , 1日时间线的时间为 {}", DateUtil.getDate(sourceRoomMessage.getMsgTime()),
//							DateUtil.getDate(DateUtil.countDayToDate(-1)));
					
					// 如果间隔时间大于1天线，当作第一次同步，只进行本次while循环。
					if (lineTime > sourceMsgTime) {
						flag = 0;// 设置flag=0，就不会进行下次while循环了。
						
//						log.info("【{}】口袋房间消息为间隔超过1天再次同步。", member.getName());
						
					}
				}

				try {
					Thread.sleep(SleepMillis.REQUEST);
				} catch (InterruptedException e) {
					log.error("线程休眠错误，异常：{}", e.toString());
				}

				List<RoomMessage> messageList = Pocket48Tool.getRoomMessageList(String.valueOf(member.getId()),
						String.valueOf(member.getRoomId()), nextTime);
				
				if (null == messageList) {
					break;
				}
				
				// 遍历保存
				for (RoomMessage message : messageList) {
					long newMsgTime = message.getMsgTime().getTime();
					// 当本次获取的消息时间小于等于旧的消息时间时，不进行下次while循环，并结束本遍历保存。
					if (newMsgTime <= sourceMsgTime) { 
						flag = 0; // 设置flag=0，就不会进行下次while循环了。
						
//						log.info("【{}】已匹配到上次最后一条数据。", member.getName());
						
						break;
					} else { // 否则需要继续进行while循环。
						nextTime = message.getMsgTime().getTime();// 设置下一循环的参数
						
//						log.info("【{}】未匹配到上次最后一条数据，需要继续执行while循环，nextTime={}", member.getName(),
//								DateUtil.getDate(message.getMsgTime()));
					}
					sendRoomMessageList.add(message); // 保存到待发送集合
				}
			}

			int size = sendRoomMessageList.size();
			if (size > 0) {// 当待发送列表中含有消息时，倒序发送到消息列表（节点倒序，时间顺序）。

				log.info("【{}】有 {} 条待发送到消息队列的口袋房间消息。", member.getName(), size);

				for (int i = size; i > 0; i--) {
					RoomMessage sendRoomMessage = sendRoomMessageList.get(i - 1);

					// 发送消息
					try {
						sendRoomMessage(sendRoomMessage);
					} catch (InterruptedException e) {
						log.error("发送{}的口袋消息到消息队列中发生错误, 异常：{}", member.getName(), e.toString());
					}

					// 写入数据库
					roomMessageRepository.save(sendRoomMessage);

					// 发送消息队列
//					try {
//						jmsMessagingTemplate.convertAndSend(new ActiveMQQueue(ActivemqPhysical.QUEUE_SEND_ROOM_MESSAGE),
//								RedisUtil.serializeObj(sendRoomMessage));
//					} catch (MessagingException | SerializationException e) {
//						log.info("发送口袋消息 [{}] 到消息队列中发生错误。{}", sendRoomMessage.getId(), e.getMessage());
//					}

				}

			}
		}

		log.info("--------------[结束] 同步口袋48房间消息任务。");

	}

	/**
	 * 发送口袋消息
	 * 
	 * @param roomMessage 消息实体
	 * @throws InterruptedException
	 */
	private void sendRoomMessage(RoomMessage roomMessage) throws InterruptedException {
		long roomId = roomMessage.getRoomId();
		Member member = memberService.getCacheByRoomId(roomId);
		List<RoomMonitorVO> voList = roomMonitorService.getCache(roomId);
		String message = deserializeMsgContent(roomMessage, member);
		IcqHttpApi icqHttpApi = bot.getAccountManager().getNonAccountSpecifiedApi();

		if (voList == null) {
			return;
		}

		for (RoomMonitorVO vo : voList) {
			QQCommunity qqCommunity = vo.getQqCommunity();
			String keywords = vo.getRoomMonitor().getKeywords();
			if (StringUtil.isNotBlank(keywords)) {// 当有关键字监控时，匹配关键字

				String[] keyword = keywords.split(",");
				for (String key : keyword) {
					if (message.contains(key)) {// 当关键字匹配成功后，发送该消息
						KuqManage.sendSyncMessage(icqHttpApi, message, qqCommunity);
						break;
					}
				}

			} else {// 当无关键字监控时，全量发送消息
				KuqManage.sendSyncMessage(icqHttpApi, message, qqCommunity);
			}
		}

		// 设置为已发送
		roomMessage.setIsSend(MsgSend.SENDED);
	}

	/**
	 * 转化消息内容
	 * 
	 * @param roomMessage 消息实体类
	 * @param member      成员实体类
	 * @return 格式化后的房间消息
	 */
	private String deserializeMsgContent(RoomMessage roomMessage, Member member) {
		StringBuffer sb = new StringBuffer();
		sb.append("来自房间：");
		sb.append(member.getRoomName());
//		sb.append("\n【话题】");
//		sb.append(member.getTopic());
		sb.append("\n");
		sb.append(roomMessage.getSenderName());
		sb.append(" ");
		sb.append(DateUtil.getDate(roomMessage.getMsgTime()));
		sb.append("\n");
		sb.append(roomMessage.getMsgContent());
		String message = sb.toString().replace("<br>", "\n");
		return message;
	}
}