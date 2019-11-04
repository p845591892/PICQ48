package com.snh48.picq.service.impl;

import java.util.Optional;

import javax.net.ssl.HttpsURLConnection;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snh48.picq.entity.QuartzConfig;
import com.snh48.picq.quartz.QuartzManage;
import com.snh48.picq.repository.QuartzConfigRepository;
import com.snh48.picq.service.QuartzConfigService;
import com.snh48.picq.utils.SpringUtil;
import com.snh48.picq.utils.StringUtil;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@Transactional
public class QuartzConfigServiceImpl implements QuartzConfigService {

	/**
	 * 定时任务配置表Repository组件
	 */
	@Autowired
	private QuartzConfigRepository quartzConfigRepository;

	@Autowired
	private QuartzManage quartzManage;

	public int updateQuartzConfig(QuartzConfig quartzConfig) throws SchedulerException {
		if (quartzConfig.getId() == null) {
			return 0;
		}
		if (StringUtil.isEmpty(quartzConfig.getJobName())) {
			return 1;
		}
		if (StringUtil.isEmpty(quartzConfig.getCronTrigger())) {
			return 2;
		}
		if (StringUtil.isEmpty(quartzConfig.getCron())) {
			return 3;
		}
		CronTrigger trigger = (CronTrigger) SpringUtil.getBean(quartzConfig.getCronTrigger());
		Scheduler scheduler = (Scheduler) SpringUtil.getBean("scheduler");
		trigger = (CronTrigger) scheduler.getTrigger(trigger.getKey());
		String currentCron = "";
		try {
			currentCron = trigger.getCronExpression();// 当前Trigger使用的规则
		} catch (NullPointerException e) {
			log.info("JobTrigger【{}】 not in org.quartz.CronTrigger.", quartzConfig.getJobName());
			return 4;
		}
		String searchCron = quartzConfig.getCron();// 从数据库查询出来的
		if (!currentCron.equals(searchCron)) {// 如果当前使用的cron表达式和从数据库中查询出来的cron表达式一致，则不刷新任务
			log.info("JobTrigger【{}】's cron change to [{}], resetting......", quartzConfig.getJobName(), searchCron);
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(searchCron);// 表达式调度构建器
			trigger = trigger.getTriggerBuilder().withIdentity(trigger.getKey()).withSchedule(scheduleBuilder).build();// 按新的cronExpression表达式重新构建trigger
			scheduler.rescheduleJob(trigger.getKey(), trigger);// 按新的trigger重新设置job执行
			log.info("Set up.");
		}
		quartzConfigRepository.save(quartzConfig);
		return HttpsURLConnection.HTTP_OK;
	}

	@Override
	public int startQuartzJob(Long id) {
		Optional<QuartzConfig> optional = quartzConfigRepository.findById(id);
		if (!optional.isPresent()) {
			return 0;
		}
		QuartzConfig job = optional.get();
		if (quartzManage.checkJob(job)) {
			return 1;
		} else {
			if (!quartzManage.startJob(job)) {
				return 2;
			}
		}
		job.setStatus(true);
		quartzConfigRepository.save(job);
		return HttpsURLConnection.HTTP_OK;
	}

	@Override
	public int shutdownQuartzJob(Long id) {
		Optional<QuartzConfig> optional = quartzConfigRepository.findById(id);
		if (!optional.isPresent()) {
			return 0;
		}
		QuartzConfig job = optional.get();
		if (!quartzManage.checkJob(job)) {
			return 1;
		} else {
			if (!quartzManage.remove(job)) {
				return 2;
			}
		}
		job.setStatus(false);
		quartzConfigRepository.save(job);
		return HttpsURLConnection.HTTP_OK;
	}
}
