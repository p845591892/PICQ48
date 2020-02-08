package com.snh48.picq.service;

import java.util.List;

import com.snh48.picq.entity.snh48.RoomMonitor;
import com.snh48.picq.vo.RoomMonitorVO;

/**
 * 口袋房间监控配置接口
 * 
 * @author shiro
 *
 */
public interface RoomMonitorService {

	/**
	 * 新增一条口袋房间监控配置，并刷新该房间的监控配置缓存。
	 * 
	 * @param roomMonitor 监控实体类
	 */
	void addRoomMonitor(RoomMonitor roomMonitor);

	/**
	 * 修改监控过滤关键字
	 * 
	 * @param id      监控配置ID
	 * @param keyword 过滤关键字
	 */
	void updateKeyword(Long id, String keyword);

	/**
	 * 删除一条监控配置
	 * 
	 * @param id 配置ID
	 */
	void deleteRoomMonitor(Long id);

	/**
	 * 获取口袋房间消息监控配置列表的缓存
	 * 
	 * @param roomId 口袋房间ID
	 * @return {@link RoomMonitorVO}的集合
	 */
	List<RoomMonitorVO> getCache(Long roomId);

	/**
	 * 获取房间监控配置
	 * @param roomMonitorId 监控配置ID
	 * @return
	 */
	RoomMonitor getById(Long roomMonitorId);

}
