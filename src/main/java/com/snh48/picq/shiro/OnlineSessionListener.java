package com.snh48.picq.shiro;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;

import lombok.extern.log4j.Log4j2;

/**
 * shiro的在线session监听
 * 
 * @author shiro
 *
 */
@Log4j2
public class OnlineSessionListener implements SessionListener {

	private final AtomicInteger sessionCount = new AtomicInteger(0);

	@Override
	public void onStart(Session session) {
		// 会话创建，在线人数加一
		sessionCount.incrementAndGet();
		log.info("创建session会话，人数+1");
		log.info("总在线人数：{}", sessionCount.get());
	}

	@Override
	public void onStop(Session session) {
		// 会话退出,在线人数减一
		sessionCount.decrementAndGet();
		log.info("关闭session会话，人数-1");
		log.info("总在线人数：{}", sessionCount.get());
	}

	@Override
	public void onExpiration(Session session) {
		// 会话过期,在线人数减一
		sessionCount.decrementAndGet();
		log.info("session会话过期，人数-1");
		log.info("总在线人数：{}", sessionCount.get());
	}

	/**
	 * 获取在线人数
	 * 
	 * @return
	 */
	public Integer getSessionCount() {
		return this.sessionCount.get();
	}

}
