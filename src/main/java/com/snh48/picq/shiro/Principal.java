package com.snh48.picq.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import com.snh48.picq.entity.system.User;

/**
 * @author shiro
 */
public class Principal {

	/**
	 * 获取用户主题
	 *
	 * @return
	 */
	public static Subject getSubject() {
		return SecurityUtils.getSubject();
	}

	/**
	 * 当前session
	 * 
	 * @return
	 */
	public static Session getSession() {
		return getSubject().getSession();
	}

	/**
	 * 获取当前登录的用户实体
	 * 
	 * @return
	 */
	public static User getCurrentUse() {
		return (User) getSession().getAttribute("user");
	}

}
