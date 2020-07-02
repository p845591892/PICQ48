package com.snh48.picq.quartz.job;

import java.util.List;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.transaction.annotation.Transactional;

import com.snh48.picq.core.Common.SleepMillis;
import com.snh48.picq.entity.weibo.WeiboUser;
import com.snh48.picq.https.WeiboTool;
import com.snh48.picq.repository.weibo.WeiboUserRepository;

import lombok.extern.log4j.Log4j2;

/**
 * 同步微博用户信息任务
 * 
 * @author shiro
 *
 */
@Log4j2
@Transactional
@DisallowConcurrentExecution // 任务串行注解
public class SyncWeiboUserJob extends QuartzJobBean {

	@Autowired
	private WeiboUserRepository weiboUserRepository;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		log.info("--------------[开始] 同步微博用户信息任务。");
		List<WeiboUser> sourceUserList = weiboUserRepository.findAll();
		for (WeiboUser sourceUser : sourceUserList) {
			try {
				Thread.sleep(SleepMillis.REQUEST);
			} catch (InterruptedException e) {
				log.error("线程休眠错误，异常：{}", e.toString());
			}
			
			WeiboUser weiboUser = WeiboTool.getUser(sourceUser.getContainerUserId());
			if (weiboUser != null) {
				weiboUserRepository.save(weiboUser);
			}
		}
		log.info("--------------[结束] 同步微博用户信息任务。");
	}

}