package com.snh48.picq.service;

import java.util.List;

import com.snh48.picq.entity.weibo.DynamicMonitor;
import com.snh48.picq.vo.DynamicMonitorVO;

/**
 * 微博动态监控服务接口
 * 
 * @author shiro
 *
 */
public interface DynamicMonitorService {

	/**
	 * 新增一位监控的微博用户
	 * 
	 * @param containerId 容器ID
	 */
	void addWeiboUser(Long containerId);

	/**
	 * 删除一位监控的微博用户
	 * 
	 * @param userId 微博用户ID
	 */
	void deleteWeiboUser(String userId);

	/**
	 * 新增一条监控配置
	 * 
	 * @param dynamicMonitor 监控配置实体
	 */
	void addDynamicMonitor(DynamicMonitor dynamicMonitor);

	/**
	 * 删除一条监控配置
	 * 
	 * @param id 监控配置ID
	 */
	void deleteDynamicMonitor(Long id);

	/**
	 * 获取微博动态监控配置列表的缓存
	 * 
	 * @param userId 微博用户ID
	 * @return {@link DynamicMonitorVO}的集合
	 */
	List<DynamicMonitorVO> getCache(long userId);

}
