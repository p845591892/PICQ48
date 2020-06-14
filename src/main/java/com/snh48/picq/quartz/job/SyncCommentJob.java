package com.snh48.picq.quartz.job;

import java.util.Date;
import java.util.List;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.transaction.annotation.Transactional;

import com.snh48.picq.core.Common.MonitorType;
import com.snh48.picq.core.Common.SleepMillis;
import com.snh48.picq.entity.snh48.Member;
import com.snh48.picq.entity.snh48.RoomMessageAll;
import com.snh48.picq.https.Pocket48Tool;
import com.snh48.picq.service.MemberService;
import com.snh48.picq.service.RoomMessageAllService;
import com.snh48.picq.utils.DateUtil;

import lombok.extern.log4j.Log4j2;

/**
 * 定时同步已开启监控的口袋房间的留言板
 * 
 * @author shiro
 *
 */
@Log4j2
@Transactional
@DisallowConcurrentExecution // 任务串行注解
public class SyncCommentJob extends QuartzJobBean {

	@Autowired
	private MemberService memberService;

	@Autowired
	private RoomMessageAllService roomMessageAllService;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		log.info("--------------[开始] 同步SNH48 Group成员口袋房间留言板任务。");

		List<Member> members = memberService.getMemberList(MonitorType.OPEN);
		
		log.info("本轮待同步留言板的成员有{}位", members.size());
		
		Date date = DateUtil.countDayToDate(-1);
		members.stream().forEach(p -> {
			long beginTime = DateUtil.getNearMidnight(date).getTime();
			long endTime = DateUtil.getMidnight(date).getTime();
			try {
				Date lastMsgDate = roomMessageAllService.getLastMessageDate(p.getRoomId());
				if (null != lastMsgDate) {
					endTime = endTime < lastMsgDate.getTime() ? endTime : lastMsgDate.getTime();
				}
				saveMessages(beginTime, endTime, p);
			} catch (Exception e) {
				log.error("同步{}口袋房间留言版消息失败，异常：{}", p.getName(), e.toString());
			}
		});

		log.info("--------------[结束] 同步SNH48 Group成员口袋房间留言板任务。");
	}

	private void saveMessages(long beginTime, long endTime, Member member) {
		int index = 0;
		do {
			index += 1;
			try {
				Thread.sleep(SleepMillis.POCKET_REQUEST);
			} catch (InterruptedException e) {
				log.error("线程休眠错误，异常：{}", e.toString());
			}
			
			List<RoomMessageAll> messages = Pocket48Tool.getRoomMessageAllList(beginTime, false, member.getRoomId());

			if (null == messages) {
				log.error("获取口袋房间留言板消息为空，beginTime={}, member={}，本轮第{}次执行。", beginTime, member.getName(), index);
				return;
			}
			try {
				roomMessageAllService.insert(messages);
			} catch (Exception e) {
				log.error("插入口袋房间留言板消息失败。参数：[beginTime={}, endTime={}, member={}]。本轮第{}次执行。异常：{}", beginTime, endTime,
						member.getName(), index, e.toString());
				return;
			}

			int size = messages.size();
			RoomMessageAll message = messages.get(size - 1);
			beginTime = message.getMessageTime().getTime();
			
		} while (beginTime >= endTime);
	}

}