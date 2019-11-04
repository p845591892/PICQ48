package com.snh48.picq.quartz.job;

import java.util.List;
import java.util.Optional;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.transaction.annotation.Transactional;

import com.snh48.picq.dao.MemberDao;
import com.snh48.picq.entity.snh48.Member;
import com.snh48.picq.https.Pocket48Tool;
import com.snh48.picq.repository.snh48.MemberRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Transactional
@DisallowConcurrentExecution // 任务串行注解
public class SyncMemberJob extends QuartzJobBean {

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private MemberDao memberDao;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		log.info("--------------开始：SyncMemberJob");

		List<Member> memberList = Pocket48Tool.getMemberList();
		if (memberList == null) {
			return;
		}
		for (Member member : memberList) {
			long memberId = member.getId();
			Optional<Member> om = memberRepository.findById(memberId);
			if (om.isPresent()) {/* 1.旧成员/原库中有记录成员 */
				Member sourceMember = om.get();
				int sourceRoomMonitor = sourceMember.getRoomMonitor();
				if (sourceRoomMonitor != 1 && sourceRoomMonitor != 2
						&& member.getRoomMonitor() == null) {/* 2.房间关闭后再开 */
					member.setRoomMonitor(2);// 强制为监控关闭
				} else if ((sourceRoomMonitor == 1 || sourceRoomMonitor == 2)
						&& member.getRoomMonitor() == null) {/* 3.房间一直开着 */
					member.setRoomMonitor(sourceRoomMonitor);// 强制使用旧状态
				}
			}
			/* 修改/新增成员资料 */
			int row = memberDao.updateMemberById(member);
			if (row == 0) {// 说明为新成员，需要再进行insert
				memberRepository.save(member);
			}
		}

		log.info("--------------结束：SyncMemberJob");
	}

}