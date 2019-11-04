package com.snh48.picq.service;

import org.quartz.SchedulerException;

import com.snh48.picq.entity.QuartzConfig;

/**
 * @Description: 定时任务配置列表操作服务类
 * @author JuFF_白羽
 * @date 2018年11月26日 下午4:20:05
 */
public interface QuartzConfigService {

	/**
	 * @Description: 修改一条定时任务配置
	 *               <p>
	 *               修改数据库数据成功后，再对定时任务调度工厂中的配置进行修改。
	 * @author lcy
	 * @throws SchedulerException
	 */
	int updateQuartzConfig(QuartzConfig quartzConfig) throws SchedulerException;

	/**
	 * 启动一条定时任务
	 * 
	 * @param id 任务ID
	 */
	int startQuartzJob(Long id);

	/**
	 * 关闭一条定时任务
	 * 
	 * @param id 任务ID
	 */
	int shutdownQuartzJob(Long id);

}
