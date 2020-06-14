package com.snh48.picq.entity.snh48;

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
 * 行程表
 * 
 * @author shiro
 *
 */
@Data
@Entity
@Table(name = "T_SNH_TRIP")
public class Trip implements Serializable {

	private static final long serialVersionUID = 7811759746625813266L;
	
	/**
	 * 主键
	 */
	@Id
	@Column(name = "ID")
	private Long id;
	
	/**
	 * 行程类型
	 * <PRE>
	 * 0：生日，1：公演，3:冷餐，5:节目
	 * </PRE>
	 */
	@Column(name = "TYPE")
	private Integer type;
	
	/**
	 * 行程标题
	 */
	@Column(name = "TITLE", length = 50)
	private String title;
	
	/**
	 * 补充标题
	 */
	@Column(name = "SUB_TITLE", length = 100)
	private String subTitle;
	
	/**
	 * 行程时间
	 */
	@Column(name = "SHOW_TIME")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date showTime;
	
	/**
	 * 行程内容
	 */
	@Column(name = "CONTENT", length = 150)
	private String content;
	
	/**
	 * 参加成员名字（,拼接）
	 */
	@Column(name = "JOIN_MEMBER_NAME", length = 300)
	private String joinMemberName;
	
	/**
	 * 门票信息
	 */
	@Column(name = "TICKET_CONTENT", length = 150)
	private String ticketContent;
	
	/**
	 * 直播信息
	 */
	@Column(name = "LIVE_CONTENT", length = 50)
	private String liveContent;

	/**
	 * 直播类型
	 */
	@Column(name = "LIVE_TYPE")
	private Integer liveType;
	
	/**
	 * 详细地址
	 */
	@Column(name = "LOCATION_DETAILS", length = 100)
	private String locationDetails;
	
	/**
	 * 地址关键字
	 */
	@Column(name = "LOCATION_KEYWORD", length = 50)
	private String locationKeyword;

}
