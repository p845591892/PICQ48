package com.snh48.picq.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snh48.picq.core.Common;
import com.snh48.picq.entity.snh48.RoomMonitor;
import com.snh48.picq.repository.snh48.RoomMonitorRepository;
import com.snh48.picq.service.RoomMonitorService;
import com.snh48.picq.utils.RedisUtil;
import com.snh48.picq.vo.RoomMonitorVO;

@Service
@Transactional
public class RoomMonitorServiceImpl implements RoomMonitorService {

	@Autowired
	private RoomMonitorRepository roomMonitorRepository;

	@Override
	public void addRoomMonitor(RoomMonitor roomMonitor) {
		// 保存监控信息
		roomMonitorRepository.save(roomMonitor);
		// 设置缓存
		setCache(roomMonitor.getRoomId());
	}

	/**
	 * 设置口袋房间监控缓存
	 * 
	 * @param roomId 口袋房间ID
	 */
	private List<RoomMonitorVO> setCache(long roomId) {
		List<RoomMonitorVO> roomMonitorList = roomMonitorRepository.findRoomMonitorAndQQCommunityByRoomId(roomId);
		String keyStr = Common.ROOM_MONITOR + String.valueOf(roomId);
		RedisUtil.setex(keyStr, roomMonitorList, Common.EXPIRE_TIME_SECOND_MONITOR);
		return roomMonitorList;
	}

	@Override
	public void updateKeyword(Long id, String keyword) {
		// 修改关键字
		roomMonitorRepository.updateKeywordById(id, keyword);
		// 设置缓存
		RoomMonitor roomMonitor = roomMonitorRepository.findById(id).get();
		setCache(roomMonitor.getRoomId());
	}

	@Override
	public void deleteRoomMonitor(Long id) {
		// 获取roomId
		RoomMonitor roomMonitor = roomMonitorRepository.findById(id).get();
		long roomId = roomMonitor.getRoomId();
		// 删除配置
		roomMonitorRepository.deleteById(id);
		// 刷新缓存
		setCache(roomId);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<RoomMonitorVO> getCache(Long roomId) {
		String keyStr = Common.ROOM_MONITOR + String.valueOf(roomId);
		if (RedisUtil.exists(keyStr)) {// 如果缓存中存在，直接返回
			return (List<RoomMonitorVO>) RedisUtil.get(keyStr);
		} else {// 缓存中不存在，获取新的
			return setCache(roomId);
		}
	}

	@Override
	public RoomMonitor getById(Long roomMonitorId) {
		Optional<RoomMonitor> optional = roomMonitorRepository.findById(roomMonitorId);
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

}
