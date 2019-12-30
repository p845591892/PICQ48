package com.snh48.picq.quartz.job;

import java.util.ArrayList;
import java.util.List;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.transaction.annotation.Transactional;

import com.snh48.picq.entity.snh48.Trip;
import com.snh48.picq.https.Pocket48Tool;
import com.snh48.picq.repository.snh48.TripRepository;

import lombok.extern.log4j.Log4j2;

/**
 * 同步SNH48 Group行程
 * <p>
 * 主要工作：进行两次查询，即APP上的两页数据，获取后存入数据库中。
 * 
 * @author shiro
 *
 */
@Log4j2
@Transactional
@DisallowConcurrentExecution // 任务串行注解
public class SyncTripJob extends QuartzJobBean {

	@Autowired
	private TripRepository tripRepository;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		log.info("--------------开始：SyncTripJob");
		
		long sourceTimestamp = 0;
		boolean isMore = false;
		List<Trip> allList = new ArrayList<>();
		
		for (int i = 0; i < 3; i++) {
			List<Trip> tripList = Pocket48Tool.getTripList(sourceTimestamp, 0, isMore);
			if (tripList == null) {
				return;
			}
			allList.addAll(tripList);
			int size = tripList.size();
			sourceTimestamp = tripList.get(size - 1).getShowTime().getTime();
			isMore = true;
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				return;
			}
		}

		for (Trip trip : allList) {
			tripRepository.save(trip);
		}

		log.info("--------------结束：SyncTripJob");
	}

}