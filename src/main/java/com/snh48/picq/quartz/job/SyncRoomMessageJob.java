package com.snh48.picq.quartz.job;

import java.util.List;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.transaction.annotation.Transactional;

import com.snh48.picq.entity.snh48.Member;
import com.snh48.picq.entity.snh48.RoomMessage;
import com.snh48.picq.https.HttpsURL;
import com.snh48.picq.https.Pocket48Tool;
import com.snh48.picq.repository.snh48.MemberRepository;
import com.snh48.picq.repository.snh48.RoomMessageRepository;
import com.snh48.picq.utils.DateUtil;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Transactional
@DisallowConcurrentExecution // 任务串行注解
public class SyncRoomMessageJob extends QuartzJobBean {

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private RoomMessageRepository roomMessageRepository;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		log.info("--------------开始：SyncRoomMessageJob");
		// 获取开启了监控的成员，逐一发送请求获取其口袋房间消息
		List<Member> members = memberRepository.findByRoomMonitor(1);
		for (Member member : members) {
			RoomMessage sourceRoomMessage = roomMessageRepository
					.findFirstByRoomIdOrderByMsgTimeDesc(member.getRoomId());
			int flag = 1;
			long nextTime = 0;
			String sourceRoomMessageId = null;
			while (flag == 1) {
				if (sourceRoomMessage == null) {// 当第一次同步时，只进行本次while循环。
					log.info("【{}】口袋房间消息为第一次同步。", member.getName());
					flag = 0;// 设置flag=0，就不会进行下次while循环了。
				} else {// 否则需要对ID进行匹配，所以这里获取数据库中最新的ID。
					log.info("【{}】口袋房间同步的最后一条消息时间为：{}", member.getName(),
							DateUtil.getDate(sourceRoomMessage.getMsgTime()));
					sourceRoomMessageId = sourceRoomMessage.getId();
				}
				try {
					Thread.sleep(HttpsURL.REQUEST_INTERVAL_TIME);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				log.info("获取【{}】口袋房间消息", member.getName());
				List<RoomMessage> messageList = Pocket48Tool.getRoomMessageList(String.valueOf(member.getId()),
						String.valueOf(member.getRoomId()), nextTime);
				// 遍历保存
				for (RoomMessage message : messageList) {
					if (sourceRoomMessageId != null && sourceRoomMessageId.equals(message.getId())) {// 当本次获取的消息ID中含有上次同步的最后一条消息ID时，不进行下次while循环。
						flag = 0;// 设置flag=0，就不会进行下次while循环了。
						break;
					} else {// 否则需要继续进行while循环。
						nextTime = message.getMsgTime().getTime();// 设置下一循环的参数
					}
					roomMessageRepository.save(message);

					// 发送消息
				}
			}
		}
		log.info("--------------结束：SyncRoomMessageJob");
	}

}