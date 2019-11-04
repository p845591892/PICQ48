package com.snh48.picq.service;

import javax.servlet.http.HttpServletRequest;

import com.snh48.picq.vo.UserVO;

public interface RegisterService {

	/**
	 * @Description: 验证用户名是否存在
	 * @author JuFF_白羽
	 * @param username 用户名
	 */
	int validateUsername(String username);

	/**
	 * @Description: 注册用户
	 * @author JuFF_白羽
	 * @param request
	 */
	int doRegister(UserVO user, HttpServletRequest request);

	/**
	 * @Description: 激活账号
	 * @author JuFF_白羽
	 * @param username     用户名
	 * @param emailCaptcha 邮箱验证码
	 */
	int activation(String username, String emailCaptcha);

}
