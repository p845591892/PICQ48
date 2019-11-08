package com.snh48.picq.activemq;

import java.util.List;

import org.crazycake.shiro.exception.SerializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.snh48.picq.entity.QQCommunity;
import com.snh48.picq.entity.snh48.Member;
import com.snh48.picq.entity.snh48.RoomMessage;
import com.snh48.picq.kuq.KuqManage;
import com.snh48.picq.repository.snh48.RoomMessageRepository;
import com.snh48.picq.service.MemberService;
import com.snh48.picq.service.RoomMonitorService;
import com.snh48.picq.utils.DateUtil;
import com.snh48.picq.utils.RedisUtil;
import com.snh48.picq.utils.StringUtil;
import com.snh48.picq.vo.RoomMonitorVO;

import cc.moecraft.icq.PicqBotX;
import cc.moecraft.icq.sender.IcqHttpApi;

/**
 * 发送口袋48房间消息的消息队列执行者
 * 
 * @author shiro
 *
 */
@Component
public class SendRoomMessageSub {

	@Autowired
	private RoomMonitorService roomMonitorService;

	@Autowired
	private MemberService memberService;

	@Autowired
	private RoomMessageRepository roomMessageRepository;

	@Autowired
	private PicqBotX bot;

	/**
	 * Jms监听器
	 * 
	 * @param bytes 对象的bytes序列
	 * @throws InterruptedException
	 * @throws SerializationException
	 */
	@JmsListener(destination = ActivemqPhysical.QUEUE_SEND_ROOM_MESSAGE)
	public void run(byte[] bytes) throws SerializationException, InterruptedException {

		Thread.sleep(2000);

		RoomMessage roomMessage = (RoomMessage) RedisUtil.deserializeObj(bytes);
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
					}
				}

			} else {// 当无关键字监控时，全量发送消息
				KuqManage.sendSyncMessage(icqHttpApi, message, qqCommunity);
			}
		}

		// 设置为已发送
		roomMessage.setIsSend(2);
		roomMessageRepository.save(roomMessage);
	}

//	/**
//	 * Jms监听器
//	 * 
//	 * @param roomId 口袋房间ID
//	 * @throws InterruptedException
//	 * @throws JMSException
//	 * @throws BeansException
//	 * @throws SerializationException
//	 */
//	@JmsListener(destination = ActivemqPhysical.QUEUE_SEND_ROOM_MESSAGE)
//	public void run(Long roomId) throws InterruptedException {
//		List<RoomMessage> roomMessageList = roomMessageRepository.findByRoomIdAndIsSendOrderByMsgTimeAsc(roomId, 1);
//		if (roomMessageList == null) {
//			return;
//		}
//		// 获取缓存的成员信息和房间监控信息
//		Member member = memberService.getCacheByRoomId(roomId);
//		List<RoomMonitorVO> voList = roomMonitorService.getCache(roomId);
//
//		log.info("[{}]口袋房间有 {} 条新消息即将发送到监控QQ(群)。", member.getName(), roomMessageList.size());
//
//		IcqHttpApi icqHttpApi = bot.getAccountManager().getNonAccountSpecifiedApi();
//		
//		for (RoomMessage roomMessage : roomMessageList) {
//			String message = deserializeMsgContent(roomMessage, member);
//
//			for (RoomMonitorVO vo : voList) {
//				QQCommunity qqCommunity = vo.getQqCommunity();
//				String keywords = vo.getRoomMonitor().getKeywords();
//				if (StringUtil.isNotBlank(keywords)) {// 当有关键字监控时，匹配关键字
//
//					String[] keyword = keywords.split(",");
//					for (String key : keyword) {
//						if (message.indexOf(key) != -1) {// 当关键字匹配成功后，发送该消息
//							KuqManage.sendSyncMessage(icqHttpApi, message, qqCommunity);
//						}
//					}
//
//				} else {// 当无关键字监控时，全量发送消息
//					KuqManage.sendSyncMessage(icqHttpApi,message, qqCommunity);
//				}
//			}
//
//			// 设置为已发送
//			roomMessage.setIsSend(2);
//			roomMessageRepository.save(roomMessage);
//		}
//	}

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
