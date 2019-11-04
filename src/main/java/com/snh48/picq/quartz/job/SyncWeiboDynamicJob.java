package com.snh48.picq.quartz.job;

import java.util.List;
import java.util.Optional;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.transaction.annotation.Transactional;

import com.snh48.picq.entity.weibo.Dynamic;
import com.snh48.picq.entity.weibo.WeiboUser;
import com.snh48.picq.https.HttpsURL;
import com.snh48.picq.https.WeiboTool;
import com.snh48.picq.repository.weibo.DynamicRepository;
import com.snh48.picq.repository.weibo.WeiboUserRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Transactional
@DisallowConcurrentExecution // 任务串行注解
public class SyncWeiboDynamicJob extends QuartzJobBean {

	@Autowired
	private WeiboUserRepository weiboUserRepository;

	@Autowired
	private DynamicRepository dynamicRepository;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		log.info("--------------开始：SyncWeiboDynamicJob");
		// 获库中保存的取所有的微博用户
		List<WeiboUser> userList = weiboUserRepository.findAll();
		for (WeiboUser weiboUser : userList) {
			try {
				Thread.sleep(HttpsURL.REQUEST_INTERVAL_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			long containerDynamicId = weiboUser.getContainerDynamicId();
			List<Dynamic> dynamiList = WeiboTool.getDynamicList(containerDynamicId);
			for (Dynamic dynami : dynamiList) {
				Optional<Dynamic> od = dynamicRepository.findById(dynami.getId());
				if (od.isPresent()) {// 当该条动态存在数据库中时,判断是否置顶
					if (dynami.getIsTop()) {// 若为置顶博，则结束本次循环
						continue;
					} else {// 否则结束循环
						break;
					}
				}
				dynamicRepository.save(dynami);

				// 发送消息
			}
		}

		log.info("--------------结束：SyncWeiboDynamicJob");
	}

}