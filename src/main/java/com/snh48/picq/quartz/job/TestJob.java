package com.snh48.picq.quartz.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Transactional
@DisallowConcurrentExecution // 任务串行注解
public class TestJob extends QuartzJobBean {

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		log.info("--------------开始：SyncMemberJob");

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		log.info("--------------结束：SyncMemberJob");
	}

}