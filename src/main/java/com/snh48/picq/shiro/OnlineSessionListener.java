package com.snh48.picq.shiro;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;

/**
 * shiro的session监听
 * 
 * @author shiro
 *
 */
public class OnlineSessionListener implements SessionListener {

	private final AtomicInteger sessionCount = new AtomicInteger(0);

	@Override
	public void onStart(Session session) {
		// 会话创建，在线人数加一
		sessionCount.incrementAndGet();
		System.out.println("创建session会话，人数+1");
		System.out.println("总在线人数：" + sessionCount.get());
	}

	@Override
	public void onStop(Session session) {
		// 会话退出,在线人数减一
		sessionCount.decrementAndGet();
		System.out.println("关闭session会话，人数-1");
		System.out.println("总在线人数：" + sessionCount.get());
	}

	@Override
	public void onExpiration(Session session) {
		// 会话过期,在线人数减一
		sessionCount.decrementAndGet();
		System.out.println("session会话过期，人数-1");
		System.out.println("总在线人数：" + sessionCount.get());
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
