package com.snh48.picq.kuq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.snh48.picq.service.QQCommunityService;

import lombok.extern.log4j.Log4j2;

/**
 * 同步QQ列表
 * 
 * @author shiro
 *
 */
@Log4j2
@Configuration
public class DataSourceQQThread extends Thread {

	@Autowired
	private QQCommunityService QQCommunityService;

	@Override
	public void run() {
		try {
			Thread.sleep(1000);
			QQCommunityService.syncQQCommunity();
			log.info("---------同步QQ列表完毕---------");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}