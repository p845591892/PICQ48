package com.snh48.picq.quartz;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.snh48.picq.entity.QuartzConfig;
import com.snh48.picq.repository.QuartzConfigRepository;
import com.snh48.picq.utils.SpringUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zhuxiaomeng
 * @date 2018/1/6.
 * @email 154040976@qq.com
 *        <p>
 *        启动数据库中已经设定为 启动状态(status:true)的任务 项目启动时init
 */
@Slf4j
@Configuration
public class DataSourceJobThread extends Thread {

	@Autowired
	QuartzConfigRepository repository;

	@Override
	public void run() {
		try {
			Thread.sleep(10000);
			QuartzManage quartzManage = (QuartzManage) SpringUtil.getBean("quartzManage");
			List<QuartzConfig> quartzConfigs = repository.findByStatus(true);
			// 开启任务
			quartzConfigs.forEach(jobs -> {
				log.info("---任务[" + jobs.getJobName() + "]系统 init--开始启动---------");
//				quartzManage.remove(jobs);/* 测试时必加该方法 */
				quartzManage.startJob(jobs);
			});
			if (quartzConfigs.size() == 0) {
				log.info("---数据库暂无启动的任务---------");
			} else {
				log.info("---任务启动完毕---------");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
