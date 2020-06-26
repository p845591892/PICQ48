package com.snh48.picq.entity.taoba;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * 桃叭集资详细表
 * 
 * @author shiro
 *
 */
@Data
@Entity
@Table(name = "TAOBA_JOIN")
public class TaobaJoin implements Serializable {

	private static final long serialVersionUID = 8628025027698471668L;

	/** 集资ID */
	@Id
	@Column(name = "ID")
	private Long id;

	/** SN码 */
	@Column(name = "SN", length = 10)
	private String sn;

	/** 集资金额 */
	@Column(name = "MONEY", length = 20)
	private String money;

	/**  */
	@Column(name = "FLOWER")
	private Integer flower;

	/** 集资时间 */
	@Column(name = "CREAT_TIME")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date creatTime;

	/** 用户ID */
	@Column(name = "USER_ID")
	private Long userid;

	/** 用户昵称 */
	@Column(name = "NICKNAME", length = 50)
	private String nickname;

	/** 用户头像 */
	@Column(name = "AVATAR", length = 200)
	private String avatar;

	/** 所属项目ID */
	@Column(name = "DETAIL_ID")
	private Long detailId;

	/**
	 * 是否发送
	 * <p>
	 * 这里是指是否执行过发送任务，并不是说一定会发送到指定的QQ
	 */
	@Column(name = "IS_SEND")
	private Boolean isSend;

}
