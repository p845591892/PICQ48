package com.snh48.picq.quartz;

import java.util.Date;
import java.util.HashSet;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import com.snh48.picq.entity.QuartzConfig;

import lombok.extern.log4j.Log4j2;

/**
 * 定时任务管理器
 * 
 * @author shiro
 *
 */
@Log4j2
@Component
public class QuartzManage {

	@Autowired
	SchedulerFactoryBean schedulerFactoryBean;

	/**
	 * true 存在 false 不存在
	 *
	 * @param
	 * @return
	 */
	public boolean checkJob(QuartzConfig job) {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		TriggerKey triggerKey = TriggerKey.triggerKey(String.valueOf(job.getId()), Scheduler.DEFAULT_GROUP);
		try {
			if (scheduler.checkExists(triggerKey)) {
				return true;
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 根据定时任务表<code>{@link QuartzConfig}</code>的配置，启动任务。
	 * 
	 * @param job 定时任务表的配置
	 * @return 启动成功返回true，否则返回false。
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean startJob(QuartzConfig job) {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		try {
			// 创建任务
			Class clazz = Class.forName(job.getClassPath());
			JobDetail jobDetail = JobBuilder.newJob(clazz).build();
			// 触发器
			TriggerKey triggerKey = TriggerKey.triggerKey(String.valueOf(job.getId()), Scheduler.DEFAULT_GROUP);
			CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey)
					.withSchedule(
							CronScheduleBuilder.cronSchedule(job.getCron()).withMisfireHandlingInstructionDoNothing())
					.build();
			// 作业调度
			scheduler.scheduleJob(jobDetail, trigger);
			// 启动
			if (!scheduler.isShutdown()) {
				scheduler.start();
				log.info("---任务[{}]启动成功-------", triggerKey.getName());
				return true;
			} else {
				log.info("---任务[{}]已经运行，请勿再次启动-------", triggerKey.getName());
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return false;
	}

	/**
	 * 更新
	 */
	public boolean updateJob(QuartzConfig job) {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		String createTime = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");

		TriggerKey triggerKey = TriggerKey.triggerKey(String.valueOf(job.getId()), Scheduler.DEFAULT_GROUP);
		try {
			if (scheduler.checkExists(triggerKey)) {
				return false;
			}

			JobKey jobKey = JobKey.jobKey(String.valueOf(job.getId()), Scheduler.DEFAULT_GROUP);

			CronScheduleBuilder schedBuilder = CronScheduleBuilder.cronSchedule(job.getCron())
					.withMisfireHandlingInstructionDoNothing();
			CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).withDescription(createTime)
					.withSchedule(schedBuilder).build();

			JobDetail jobDetail = scheduler.getJobDetail(jobKey);
			HashSet<Trigger> triggerSet = new HashSet<>();
			triggerSet.add(trigger);
			scheduler.scheduleJob(jobDetail, triggerSet, true);
			log.info("---任务[" + triggerKey.getName() + "]更新成功-------");
			return true;
		} catch (SchedulerException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 删除
	 */
	public boolean remove(QuartzConfig job) {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		TriggerKey triggerKey = TriggerKey.triggerKey(String.valueOf(job.getId()), Scheduler.DEFAULT_GROUP);
		try {
			if (checkJob(job)) {
				scheduler.pauseTrigger(triggerKey);
				scheduler.unscheduleJob(triggerKey);
				scheduler.deleteJob(JobKey.jobKey(String.valueOf(job.getId()), Scheduler.DEFAULT_GROUP));
				log.info("---任务[" + triggerKey.getName() + "]删除成功-------");
				return true;
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		return false;
	}

}