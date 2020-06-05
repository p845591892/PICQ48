package com.snh48.picq.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snh48.picq.dao.SystemManageDao;
import com.snh48.picq.entity.system.User;
import com.snh48.picq.repository.system.UserRepository;
import com.snh48.picq.service.RegisterService;
import com.snh48.picq.utils.MathUtil;
import com.snh48.picq.utils.StringUtil;
import com.snh48.picq.vo.UserVO;

import lombok.extern.log4j.Log4j2;

/**
 * @Description: 注册相关服务类
 * @author JuFF_白羽
 * @date 2018年12月7日 下午5:14:04
 */
@Log4j2
@Service
@Transactional
public class RegisterServiceImpl implements RegisterService {

	/**
	 * 用户表DAO组件
	 */
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private SystemManageDao systemManageDao;

	public int validateUsername(String username) {
		if (username == null || username.trim().equals("")) {
			return 2;
		}
		User user = userRepository.findByUsername(username);
		if (user != null) {
			return 1;
		} else {
			return 0;
		}
	}

	public int doRegister(UserVO vo, HttpServletRequest request) {
		if (vo.getUsername() == null) {
			return 2;
		}
		if (vo.getPassword() == null) {
			return 3;
		}
		if (vo.getNickname() == null) {
			return 4;
		}
		if (vo.getEmail() == null) {
			return 5;
		}

		User user = new User();
		user.setNickname(vo.getNickname());
		user.setPassword(StringUtil.shiroMd5(vo.getUsername(), vo.getPassword()));
		user.setUsername(vo.getUsername());
		user.setState((byte) 1);
		user.setEmail(vo.getEmail());
		String emailCaptcha = MathUtil.random(6);
		user.setEmailCaptcha(emailCaptcha);

		try {
			user = userRepository.save(user);
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("uid", user.getId());
			param.put("rids", new String[] { "6" });
			systemManageDao.insertUserRole(param);
			return 1;
		} catch (Exception e) {
			log.error("新增用户失败，username：{}, 异常：{}", vo.getUsername(), e.toString());
			return 0;
		}
	}

	public int activation(String username, String emailCaptcha) {

		if (username == null || emailCaptcha == null) {
			return 2;
		}

		User user = userRepository.findByUsername(username);
		if (user == null) {
			return 3;
		}

		if (user.getEmailCaptcha().equals(emailCaptcha)) {
			user.setEmailCaptcha(MathUtil.random(6));
			user.setState((byte) 1);
			userRepository.save(user);
			return 1;
		} else {
			return 3;
		}

	}

}
