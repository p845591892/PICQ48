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
 * 桃叭项目表
 * 
 * @author shiro
 *
 */
@Data
@Entity
@Table(name = "TAOBA_DETAIL")
public class TaobaDetail implements Serializable {

	private static final long serialVersionUID = -6249349841735986886L;

	/** 项目ID */
	@Id
	@Column(name = "ID")
	private Long id;

	/** 项目URL */
	@Column(name = "DETAIL_URL", length = 200, updatable = false)
	private String detailUrl;

	/** 项目封面URL */
	@Column(name = "POSTER", length = 200)
	private String poster;
	
	/** 项目标题 */
	@Column(name = "TITLE", length = 100)
	private String title;

	/** 项目描述 */
	@Column(name = "DESCRIPTION", columnDefinition = "text")
	private String description;

	/** 开始时间 */
	@Column(name = "START_TIME")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date startTime;

	/** 结束时间 */
	@Column(name = "END_TIME")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date endTime;

	/** 集资金额 */
	@Column(name = "DONATION", length = 20)
	private String donation;

	/** 目标金额 */
	@Column(name = "AMOUNT", length = 20)
	private String amount;

	/** 项目发起人手机号 */
	@Column(name = "PHONE", length = 11)
	private String phone;

	/** 项目发起人ID */
	@Column(name = "USER_ID")
	private Long userId;

	/** 项目发起人昵称 */
	@Column(name = "NICKNAME", length = 50)
	private String nickname;
	
	/** 项目发起人头像URL */
	@Column(name = "AVATAR", length = 200)
	private String avatar;

	/** 已售件数 */
	@Column(name = "SELLSTATS")
	private Integer sellstats;

	/** 总件数 */
	@Column(name = "TOTALSTATS")
	private Integer totalstats;
	
	/**
	 * 完成度(%)
	 */
	@Column(name = "PERCENT")
	private Double percent;

	/** OS类型 */
	@Column(name = "OS_TYPE")
	private Integer osType;
	
	/**
	 * 项目进行中
	 */
	@Column(name = "RUNNING")
	private Boolean running;
	
}