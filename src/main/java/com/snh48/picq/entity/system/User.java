package com.snh48.picq.entity.system;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * @Description: 用户表
 * @author JuFF_白羽
 * @date 2018年10月31日 下午3:12:33
 */
@Entity
@Table(name = "T_SYS_USER")
public class User implements Serializable {

	private static final long serialVersionUID = -7983626628507034805L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private Long id;

	@Column(name = "USERNAME", unique = true, length = 50)
	private String username;// 帐号

	@Column(name = "NICKNAME", length = 50)
	private String nickname;// 名称（昵称或者真实姓名，不同系统不同定义）

	@Column(name = "PASSWORD", length = 50)
	private String password; // 密码;

	@Column(name = "SALT")
	private String salt;// 加密密码的盐

	@Column(name = "STATE")
	private byte state;// 用户状态,0:创建未认证（比如没有激活，没有输入验证码等等）--等待验证的用户 ,
						// 1:正常状态,2：用户被锁定.

	@Column(name = "EMAIL_CAPTCHA", length = 20)
	private String emailCaptcha;// 电子邮箱的验证码

	@Column(name = "EMAIL", length = 100)
	private String email;// 电子邮箱

	@ManyToMany(fetch = FetchType.EAGER) // 立即从数据库中进行加载数据;
	@JoinTable(name = "t_sys_user_role", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = {
			@JoinColumn(name = "role_id") })
	private List<Role> roles;// 一个用户具有多个角色

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public byte getState() {
		return state;
	}

	public void setState(byte state) {
		this.state = state;
	}

	public String getEmailCaptcha() {
		return emailCaptcha;
	}

	public void setEmailCaptcha(String emailCaptcha) {
		this.emailCaptcha = emailCaptcha;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

}
