package com.snh48.picq.controller;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.snh48.picq.repository.snh48.MemberRepository;
import com.snh48.picq.vo.ResultVO;

/**
 * @ClassName: MemberApi
 * @Description: 成员表操作控制类
 *               <p>
 *               主要用于提供成员表的增、删、改接口。
 * @author JuFF_白羽
 * @date 2018年7月24日 下午4:46:04
 */
@RestController
@RequestMapping("/member")
public class MemberContorller {

	@Autowired
	private MemberRepository memberRepository;

	/**
	 * @Description: 修改房间监控状态
	 * @author JuFF_白羽
	 */
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
	@GetMapping("/refresh")
	public ResultVO refreshMember(HttpServletRequest request) {
		ResultVO result = new ResultVO();
//		httpsService.syncMember();
//		result.setStatus(HttpsURLConnection.HTTP_OK);
		result.setStatus(HttpsURLConnection.HTTP_BAD_REQUEST);
		result.setCause("该接口维护中");
		return result;
	}

}
