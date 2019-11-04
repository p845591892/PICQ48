package com.snh48.picq.controller;

import java.io.IOException;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.snh48.picq.service.RegisterService;
import com.snh48.picq.vo.ResultVO;
import com.snh48.picq.vo.UserVO;

/**
 * @Description: 注册相关控制类
 * @author JuFF_白羽
 * @date 2018年12月7日 下午5:04:20
 */
@RestController
public class RegisterContorller {

	/**
	 * 注册相关服务类
	 */
	@Autowired
	private RegisterService registerService;

	/**
	 * @Description: 验证用户名是否可用
	 * @author JuFF_白羽
	 */
	@PostMapping("/register/validate-username")
	public ResultVO validateUsername(String username) {
		ResultVO result = new ResultVO();
		int i = registerService.validateUsername(username);
		if (i == 1) {
			result.setStatus(400);
			result.setCause("用户名已存在");
		} else if (i == 0) {
			result.setStatus(HttpsURLConnection.HTTP_OK);
		} else if (i == 2) {
			result.setStatus(400);
			result.setCause("用户名不能为空");
		}
		return result;
	}

	/**
	 * @Description: 注册账号
	 * @author JuFF_白羽
	 */
	@PostMapping("/doRegister")
	public ResultVO doRegister(UserVO user, HttpServletRequest request) {
		ResultVO result = new ResultVO();
		int i = registerService.doRegister(user,request);
		result.setStatus(400);
		if (i == 1) {
			result.setStatus(HttpsURLConnection.HTTP_OK);
		} else if (i == 2) {
			result.setCause("用户名不能为空");
		} else if (i == 3) {
			result.setCause("密码不能为空");
		} else if (i == 4) {
			result.setCause("昵称不能为空");
		} else if (i == 5) {
			result.setCause("邮箱不能为空");
		} else {
			result.setCause("注册失败");
		}
		return result;
	}

	/**
	 * @Description: 激活账号
	 * @author JuFF_白羽
	 * @param username
	 *            用户名
	 * @param emailCaptcha
	 *            邮箱验证码
	 * @throws IOException
	 */
	@GetMapping("/register/activation")
	public ResultVO activation(String username, String emailCaptcha, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		int i = registerService.activation(username, emailCaptcha);
		String write = "";
		if (i == 2) {
			write = "请求失败，请重新确认邮件中的链接。";
		} else if (i == 3) {
			write = "请求错误，请联系系统管理员确认。";
		} else if (i == 1) {
			String url = "http://" + request.getServerName() + ":" + request.getServerPort() + "/login";
			write = "激活成功<br>点击<a href=\"" + url + "\">这里</a>跳转登录。";
		}
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		response.getWriter().write(write);
		return null;
	}

}
