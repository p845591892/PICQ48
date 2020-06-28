package com.snh48.picq.service;

import java.util.Date;
import java.util.List;

import com.snh48.picq.entity.taoba.TaobaDetail;
import com.snh48.picq.entity.taoba.TaobaJoin;
import com.snh48.picq.entity.taoba.TaobaMonitor;
import com.snh48.picq.vo.TaobaMonitorVO;

/**
 * 桃叭服务接口
 * 
 * @author shiro
 *
 */
public interface TaobaService {

	/**
	 * 获取桃叭项目列表
	 * 
	 * @param running 项目是否进行中
	 * @return {@link TaobaDetail}集合
	 */
	List<TaobaDetail> getDetailsByRunningNot(boolean running);

	/**
	 * 插入/更新桃叭项目
	 * 
	 * @param detail 桃叭项目实例
	 */
	void saveDetail(TaobaDetail detail);

	/**
	 * 获取桃叭集资详细对象
	 * 
	 * @param id 集资ID
	 * @return {@link TaobaJoin}
	 */
	TaobaJoin getJoin(long id);

	/**
	 * 获取当前最新的一条集资详细的时间
	 * 
	 * @param detailId 所属项目ID
	 * @return {@link Date}
	 */
	Date getCurrentJoinCreatTime(long detailId);

	/**
	 * 插入/更新集资详细列表
	 * 
	 * @param joins 集资详细列表
	 */
	void saveJoins(List<TaobaJoin> joins);

	/**
	 * 获取桃叭监控配置
	 * 
	 * @param detailId 桃叭项目ID
	 * @return {@link TaobaMonitorVO}集合
	 */
	List<TaobaMonitorVO> getCacheTaobaMonitor(long detailId);

	/**
	 * 获取桃叭项目列表
	 * 
	 * @return {@link TaobaDetail}集合
	 */
	List<TaobaDetail> getDetails();

	/**
	 * 插入/更新桃叭监控配置
	 */
	void saveMonitor(TaobaMonitor monitor);

	/**
	 * 删除桃叭监控配置
	 * 
	 * @param monitorId 监控配置ID
	 * @param detailId  桃叭项目ID
	 */
	void deleteMonitor(long monitorId, long detailId);

	/**
	 * 新增一个桃叭项目
	 * 
	 * @param detailUrl 集资链接
	 */
	int add(String detailUrl);

	/**
	 * 删除桃叭项目
	 * 
	 * @param detailIds 项目ID，多个用逗号间隔
	 */
	void delete(String detailIds);

	/**
	 * 获取桃叭项目
	 * 
	 * @param id 项目ID
	 * @return {@link TaobaDetail}
	 */
	TaobaDetail getDetail(long id);

}
