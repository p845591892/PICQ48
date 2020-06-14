package com.snh48.picq.quartz.job;

import java.text.ParseException;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.transaction.annotation.Transactional;

import com.snh48.picq.service.WebService;

import lombok.extern.log4j.Log4j2;

/**
 * PICQ48系统定时任务类
 * 
 * @author shiro
 *
 */
@Log4j2
@Transactional
@DisallowConcurrentExecution // 任务串行注解
public class SystemJob extends QuartzJobBean {

	@Autowired
	private WebService webService;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		log.info("--------------[开始] 系统任务");

		webService.getMtboxData(); // 刷新主页.mtbox的数据到缓存
		try {
			webService.getDsData(); // 刷新主页.ds的数据到缓存
		} catch (ParseException e) {
			log.error("定时任务：系统任务执行异常，刷新主页DS数据到Redis中失败，异常：{}", e.toString());
		}

		log.info("--------------[结束] 系统任务");
	}

}