package com.snh48.picq.controller;

import java.util.Optional;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.snh48.picq.annotation.Log;
import com.snh48.picq.annotation.OperationType;
import com.snh48.picq.core.Common.MonitorType;
import com.snh48.picq.entity.snh48.Member;
import com.snh48.picq.https.JsonPICQ48;
import com.snh48.picq.https.Pocket48Tool;
import com.snh48.picq.repository.snh48.MemberRepository;
import com.snh48.picq.service.HttpsService;
import com.snh48.picq.vo.ResultVO;

import lombok.extern.log4j.Log4j2;

/**
 * @ClassName: MemberApi
 * @Description: 成员表操作控制类
 *               <p>
 *               主要用于提供成员表的增、删、改接口。
 * @author JuFF_白羽
 * @date 2018年7月24日 下午4:46:04
 */
@Log4j2
@RestController
@RequestMapping("/member")
public class MemberContorller {

	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	private HttpsService httpsService;

	/**
	 * @Description: 修改房间监控状态
	 * @author JuFF_白羽
	 */
	@Log(desc = "修改口袋房间监控状态", type = OperationType.UPDATE)
	@PostMapping("/update/room-monitor")
	public ResultVO updateRoomMonitor(@RequestParam Long id, @RequestParam int roomMonitor) {
		ResultVO result = new ResultVO();
		int i = memberRepository.updateRoomMonitorById(id, roomMonitor);
		if (i == 1) {
			result.setStatus(HttpsURLConnection.HTTP_OK);
			result.setCause("成功");
		} else {
			result.setStatus(400);
			result.setCause("发生错误");
		}
		return result;
	}

	/**
	 * @Description: 同步成员信息到最新
	 * @author JuFF_白羽
	 */
	@Log(desc = "更新全体成员信息", type = OperationType.UPDATE)
	@GetMapping("/refresh")
	public ResultVO refreshMember(HttpServletRequest request) {
		ResultVO result = new ResultVO();
//		httpsService.syncMember();
//		result.setStatus(HttpsURLConnection.HTTP_OK);
		result.setStatus(HttpsURLConnection.HTTP_BAD_REQUEST);
		result.setCause("该接口维护中");
		return result;
	}

	/**
	 * 新增成员信息
	 * 
	 * @param id 成员ID
	 */
	@Log(desc = "新增成员信息", type = OperationType.ADD)
	@PutMapping("/add")
	public ResultVO addMember(Long id) {
		Member member = Pocket48Tool.getMember(id);
		
		if (member == null) {
			return new ResultVO(HttpsURLConnection.HTTP_NOT_FOUND, "获取的成员信息为空。");
		}
		
		long roomId = httpsService.getRoomId(member.getId());
		
		try {
			if (roomId >= 1) {
				JSONObject roomObj = JsonPICQ48.jsonMemberRoom(roomId, 0);
				member.buildRoom(roomObj);
			}
		} catch (Exception e) {
			log.error("获取成员房间信息失败，roomId={}, 异常：{}", roomId, e.getMessage());
		}

		if (member.getRoomMonitor() == null) {// 房间状态不为404
			Optional<Member> optional = memberRepository.findById(id);
			if (optional.isPresent()) {
				member.setRoomMonitor(optional.get().getRoomMonitor());
			} else {
				member.setRoomMonitor(MonitorType.CLOS);// 默认未开启同步
			}
		}

		memberRepository.save(member);

		return new ResultVO(HttpsURLConnection.HTTP_OK, "success");
	}

}
