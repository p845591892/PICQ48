package com.snh48.picq.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * @Description: 用户VO类
 *               <p>
 *               用于代替用户实体接收参数。
 * @author JuFF_白羽
 * @date 2018年11月28日 上午11:43:18
 */
@Data
public class UserVO implements Serializable {

	private static final long serialVersionUID = -7016378461529795426L;

	private Long id;

	private String username;// 帐号

	private String nickname;// 名称（昵称或者真实姓名，不同系统不同定义）

	private String password; // 密码;

	private String salt;// 加密密码的盐

	private String email;// 邮箱

	private byte state;

}
