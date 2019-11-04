package com.snh48.picq.controller;

import javax.net.ssl.HttpsURLConnection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.snh48.picq.entity.snh48.RoomMonitor;
import com.snh48.picq.repository.snh48.RoomMonitorRepository;
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
	private RoomMonitorRepository roomMonitorRepository;

	/**
	 * @Title: addRoomMonitor
	 * @Description: 新增一条房间配置
	 * @author JuFF_白羽
	 * @param roomMonitor 配置信息
	 */
	@PostMapping("/add")
	public ResultVO addRoomMonitor(RoomMonitor roomMonitor) {
		ResultVO vo = new ResultVO();
		if (roomMonitor.getCommunityId() != null && roomMonitor.getRoomId() != null) {
			RoomMonitor newRoomMonitor = roomMonitorRepository.save(roomMonitor);
			if (newRoomMonitor != null) {
				vo.setStatus(HttpsURLConnection.HTTP_OK);
			} else {
				vo.setStatus(400);
				vo.setCause("新增失败");
			}
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
	 * @param id 监控配置ID
	 * @param keyword 过滤关键字
	 */
	@PostMapping("/update/keyword")
	public ResultVO updateKeyword(Long id, String keyword) {
		ResultVO vo = new ResultVO();
		if (id != null) {
			int i = roomMonitorRepository.updateKeywordById(id, keyword);
			if (i == 1) {
				vo.setStatus(HttpsURLConnection.HTTP_OK);
			} else {
				vo.setStatus(400);
				vo.setCause("修改失败");
			}
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
	@PostMapping("/delete")
	public ResultVO deleteRoomMonitor(Long id) {
		ResultVO vo = new ResultVO();
		if (id != null) {
			roomMonitorRepository.deleteById(id);
			vo.setStatus(HttpsURLConnection.HTTP_OK);
		} else {
			vo.setStatus(400);
			vo.setCause("ID不能为空");
		}
		return vo;
	}
	
}
