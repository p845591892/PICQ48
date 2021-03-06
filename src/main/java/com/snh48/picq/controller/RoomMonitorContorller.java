package com.snh48.picq.controller;

import javax.net.ssl.HttpsURLConnection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.snh48.picq.annotation.Log;
import com.snh48.picq.annotation.OperationType;
import com.snh48.picq.entity.snh48.RoomMonitor;
import com.snh48.picq.service.RoomMonitorService;
import com.snh48.picq.vo.ResultVO;

/**
 * @ClassName: RoomMonitorApi
 * @Description: 房间监控数据操作接口
 * @author JuFF_白羽
 * @date 2018年7月26日 上午10:54:18
 */
@RestController
@RequestMapping("/room-monitor")
public class RoomMonitorContorller {

	@Autowired
	private RoomMonitorService roomMonitorService;

	/**
	 * @Title: addRoomMonitor
	 * @Description: 新增一条房间配置
	 * @author JuFF_白羽
	 * @param roomMonitor 配置信息
	 */
	@Log(desc = "新增口袋房间监控配置", type = OperationType.ADD)
	@PostMapping("/add")
	public ResultVO addRoomMonitor(RoomMonitor roomMonitor) {
		ResultVO vo = new ResultVO();
		if (roomMonitor.getCommunityId() != null && roomMonitor.getRoomId() != null) {
			roomMonitorService.addRoomMonitor(roomMonitor);
			vo.setStatus(HttpsURLConnection.HTTP_OK);
		} else {
			vo.setStatus(400);
			vo.setCause("QQ号或者房间ID不能为空");
		}
		return vo;
	}

	/**
	 * @Title: updateKeyword
	 * @Description: 修改监控过滤关键字
	 * @author JuFF_白羽
	 * @param id      监控配置ID
	 * @param keyword 过滤关键字
	 */
	@Log(desc = "修改口袋房间监控的关键字配置", type = OperationType.UPDATE)
	@PostMapping("/update/keyword")
	public ResultVO updateKeyword(Long id, String keyword) {
		ResultVO vo = new ResultVO();
		if (id != null) {
			roomMonitorService.updateKeyword(id, keyword);
			vo.setStatus(HttpsURLConnection.HTTP_OK);
		} else {
			vo.setStatus(400);
			vo.setCause("监控配置ID不能为空");
		}
		return vo;
	}

	/**
	 * @Title: deleteRoomMonitor
	 * @Description: 删除一条监控配置
	 * @author JuFF_白羽
	 * @param id 配置ID
	 */
	@Log(desc = "删除口袋房间监控配置", type = OperationType.UPDATE)
	@PostMapping("/delete")
	public ResultVO deleteRoomMonitor(Long id) {
		ResultVO vo = new ResultVO();
		if (id != null) {
			roomMonitorService.deleteRoomMonitor(id);
			vo.setStatus(HttpsURLConnection.HTTP_OK);
		} else {
			vo.setStatus(400);
			vo.setCause("ID不能为空");
		}
		return vo;
	}

}
