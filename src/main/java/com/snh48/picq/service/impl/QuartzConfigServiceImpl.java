package com.snh48.picq.service.impl;

import java.util.Optional;

import javax.net.ssl.HttpsURLConnection;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snh48.picq.entity.QuartzConfig;
import com.snh48.picq.quartz.QuartzManage;
import com.snh48.picq.repository.QuartzConfigRepository;
import com.snh48.picq.service.QuartzConfigService;

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

	public int saveQuartzConfig(QuartzConfig quartzConfig) throws SchedulerException {
		quartzConfigRepository.save(quartzConfig);
		return 1;
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

	@Override
	public QuartzConfig findById(Long id) {
		Optional<QuartzConfig> oq = quartzConfigRepository.findById(id);
		if (oq.isPresent()) {
			return oq.get();
		}
		return null;
	}

	@Override
	public void delete(Long id) {
		quartzConfigRepository.deleteById(id);
	}
}
