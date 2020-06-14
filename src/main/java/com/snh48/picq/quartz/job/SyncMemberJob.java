package com.snh48.picq.quartz.job;

import java.util.List;
import java.util.Optional;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.transaction.annotation.Transactional;

import com.snh48.picq.core.Common.MonitorType;
import com.snh48.picq.dao.MemberDao;
import com.snh48.picq.entity.snh48.Member;
import com.snh48.picq.https.JsonPICQ48;
import com.snh48.picq.https.Pocket48Tool;
import com.snh48.picq.repository.snh48.MemberRepository;
import com.snh48.picq.service.HttpsService;

import lombok.extern.log4j.Log4j2;

/**
 * 同步SNH48成员信息任务
 * 
 * @author shiro
 *
 */
@Log4j2
@Transactional
@DisallowConcurrentExecution // 任务串行注解
public class SyncMemberJob extends QuartzJobBean {

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private MemberDao memberDao;
	
	@Autowired
	private HttpsService httpsService;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		log.info("--------------[开始] 同步SNH48 Group成员信息任务。");

		List<Member> memberList = Pocket48Tool.getMemberList();
		if (memberList == null) {
			return;
		}
		
		for (Member member : memberList) {
			long memberId = member.getId();
			long roomId = httpsService.getRoomId(memberId);
			
			if (roomId < 1) { //获取不到房间ID
				memberDao.updateMemberById(member);
				continue;
			}
			
			try {
				JSONObject roomObj = JsonPICQ48.jsonMemberRoom(roomId, 0);
				member.buildRoom(roomObj);
			} catch (Exception e) {
				log.error("获取{}房间信息失败或者构建Member失败，异常：{}", member.getName(), e.toString());
				continue;
			}
			
			Optional<Member> om = memberRepository.findById(memberId);
			/* 1.旧成员/原库中有记录成员 */
			if (om.isPresent()) {
				Member sourceMember = om.get();
				int sourceRoomMonitor = sourceMember.getRoomMonitor();
				
				/* 2.房间关闭后再开 */
				if (sourceRoomMonitor != MonitorType.OPEN && sourceRoomMonitor != MonitorType.CLOS
						&& member.getRoomMonitor() == null) {
					member.setRoomMonitor(MonitorType.CLOS); // 强制为监控关闭
					
				/* 3.房间一直开着 */
				} else if ((sourceRoomMonitor == MonitorType.OPEN || sourceRoomMonitor == MonitorType.CLOS)
						&& member.getRoomMonitor() == null) {
					member.setRoomMonitor(sourceRoomMonitor); // 强制使用旧状态
				}
			}
			
			/* 修改/新增成员资料 */
			int row = memberDao.updateMemberById(member);
			if (row == 0) {// 说明为新成员，需要再进行insert
				memberRepository.save(member);
			}
		}

		log.info("--------------[结束] 同步SNH48 Group成员信息任务。");
	}

}