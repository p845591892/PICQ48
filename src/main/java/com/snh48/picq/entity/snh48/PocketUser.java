package com.snh48.picq.entity.snh48;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 口袋48用户表
 * 
 * @author shiro
 *
 */
@Data
@Entity
@Table(name = "POCKET_USER")
public class PocketUser implements Serializable{
	
	private static final long serialVersionUID = -409784986993313538L;

	/**
	 * ID
	 */
	@Id
	@Column(name = "ID")
	private Long id;
	
	/**
	 * 昵称
	 */
	@Column(name = "NICKNAME", length = 100)
	private String nickname;
	
	/**
	 * 头像地址
	 */
	@Column(name = "AVATAR", length = 500)
	private String avatar;
	
	/**
	 * 等级
	 */
	@Column(name = "LEVEL")
	private Integer level;
	
	/**
	 * 是否是VIP用户
	 */
	@Column(name = "VIP")
	private Boolean vip;
	
	/**
	 * 角色类型
	 */
	@Column(name = "ROLE")
	private Integer role;
	
	/**
	 * 信息版本
	 */
	@Column(name = "VERSION")
	private Long version;

}
