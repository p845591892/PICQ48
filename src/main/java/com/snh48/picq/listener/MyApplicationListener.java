package com.snh48.picq.listener;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.snh48.picq.kuq.DataSourceQQThread;
import com.snh48.picq.kuq.SendUpdateMessageThread;
import com.snh48.picq.quartz.DataSourceJobThread;

import lombok.extern.log4j.Log4j2;

/**
 * @author zhuxiaomeng
 * @date 2018/1/6.
 * @email 154040976@qq.com
 *        <p>
 *        通过监听，开辟线程，执行定时任务 当然 也可以执行其他
 */
@Log4j2
@Component
public class MyApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		log.info("-------------bean初始化完毕-------------");
		/**
		 * 通过线程开启数据库中已经开启的定时任务 灵感来自spring spring boot继续执行 mythread开辟线程，延迟后执行
		 */
		ApplicationContext applicationContext = event.getApplicationContext();

		// 自动开启定时任务
		DataSourceJobThread jobThread = applicationContext.getBean(DataSourceJobThread.class);
		new Thread(jobThread).start();

		// 自动同步酷Q好友和群
		DataSourceQQThread qqThread = applicationContext.getBean(DataSourceQQThread.class);
		new Thread(qqThread).start();
		
		// 自动发送重启消息给QQ好友和群
		SendUpdateMessageThread sendUpdateMsgThread = applicationContext.getBean(SendUpdateMessageThread.class);
		new Thread(sendUpdateMsgThread).start();
	}

}
