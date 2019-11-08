package com.snh48.picq.quartz.job;

import java.util.ArrayList;
import java.util.List;

import org.apache.activemq.command.ActiveMQQueue;
import org.crazycake.shiro.exception.SerializationException;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.messaging.MessagingException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.transaction.annotation.Transactional;

import com.snh48.picq.activemq.ActivemqPhysical;
import com.snh48.picq.entity.snh48.Member;
import com.snh48.picq.entity.snh48.RoomMessage;
import com.snh48.picq.https.HttpsURL;
import com.snh48.picq.https.Pocket48Tool;
import com.snh48.picq.repository.snh48.MemberRepository;
import com.snh48.picq.repository.snh48.RoomMessageRepository;
import com.snh48.picq.utils.DateUtil;
import com.snh48.picq.utils.RedisUtil;
import com.snh48.picq.utils.StringUtil;

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
	private JmsMessagingTemplate jmsMessagingTemplate;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		log.info("--------------开始：SyncRoomMessageJob");
		// 获取开启了监控的成员，逐一发送请求获取其口袋房间消息
		List<Member> members = memberRepository.findByRoomMonitor(1);
		for (Member member : members) {
			RoomMessage sourceRoomMessage = roomMessageRepository
					.findFirstByRoomIdOrderByMsgTimeDesc(member.getRoomId());

			int flag = 1;// while循环的flag
			long nextTime = 0;// https请求参数
			String sourceRoomMessageId = null;// 库中最新历史消息ID
			List<RoomMessage> sendRoomMessageList = new ArrayList<RoomMessage>();// 待发送的房间消息集合

			while (flag == 1) {
				if (sourceRoomMessage == null) {// 历史消息不存在，即，当第一次同步时，只进行本次while循环。
					log.info("【{}】口袋房间消息为第一次同步。", member.getName());
					flag = 0;// 设置flag=0，就不会进行下次while循环了。

				} else {// 历史消息存在
					long sourceMsgTime = sourceRoomMessage.getMsgTime().getTime();
					long lineTime = DateUtil.countDayToDate(-1).getTime();

//					log.info("历史消息时间为 {} , 1日时间线的时间为 {}", DateUtil.getDate(sourceRoomMessage.getMsgTime()),
//							DateUtil.getDate(DateUtil.countDayToDate(-1)));

					if (lineTime > sourceMsgTime) {// 如果间隔时间大于1天线，当作第一次同步，只进行本次while循环。
						flag = 0;// 设置flag=0，就不会进行下次while循环了。
						log.info("【{}】口袋房间消息为间隔超过1天再次同步。", member.getName());
					}

					// 否则需要对ID进行匹配，所以这里获取数据库中最新消息的ID。
					sourceRoomMessageId = sourceRoomMessage.getId();
//					log.info("【{}】口袋房间同步的最后一条消息ID为：{}", member.getName(), sourceRoomMessageId);
				}

				try {
					Thread.sleep(HttpsURL.REQUEST_INTERVAL_TIME);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

//				log.info("获取【{}】口袋房间消息，nextTime={}", member.getName(), nextTime);
				List<RoomMessage> messageList = Pocket48Tool.getRoomMessageList(String.valueOf(member.getId()),
						String.valueOf(member.getRoomId()), nextTime);
				// 遍历保存
				for (RoomMessage message : messageList) {
//					log.info("历史消息ID={} , 现在消息ID={}", sourceRoomMessageId, message.getId());
					if (StringUtil.isNotBlank(sourceRoomMessageId) && sourceRoomMessageId.equals(message.getId())) {// 当本次获取的消息ID中含有上次同步的最后一条消息ID时，不进行下次while循环。
						flag = 0;// 设置flag=0，就不会进行下次while循环了。
						log.info("【{}】已匹配到上次最后一条数据，结束while循环。", member.getName());
						break;
					} else {// 否则需要继续进行while循环。
//						log.info("【{}】未匹配到上次最后一条数据，需要继续执行while循环，nextTime={}", member.getName(),
//								DateUtil.getDate(message.getMsgTime()));
						nextTime = message.getMsgTime().getTime();// 设置下一循环的参数
					}
					roomMessageRepository.save(message);// 写入数据库
					sendRoomMessageList.add(message);// 保存到待发送集合
				}
			}

			int size = sendRoomMessageList.size();
			if (size > 0) {// 当待发送列表中含有消息时，倒序发送到消息列表（节点倒序，时间顺序）。

				log.info("【{}】有 {} 条待发送到消息队列的口袋房间消息。", member.getName(), size);

				for (int i = size; i > 0; i--) {
					RoomMessage sendRoomMessage = sendRoomMessageList.get(i - 1);
					try {
						jmsMessagingTemplate.convertAndSend(new ActiveMQQueue(ActivemqPhysical.QUEUE_SEND_ROOM_MESSAGE),
								RedisUtil.serializeObj(sendRoomMessage));
					} catch (MessagingException | SerializationException e) {
						log.info("发送【{}】的口袋消息到消息队列中发生错误。", member.getName());
						e.printStackTrace();
					}
				}

			}
		}

		log.info("--------------结束：SyncRoomMessageJob");

	}
}