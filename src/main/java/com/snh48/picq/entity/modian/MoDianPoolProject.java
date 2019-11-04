package com.snh48.picq.entity.modian;

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
 * @ClassName: MoDianPoolProject
 * @Description: 摩点集资项目表
 *               <p>
 *               用于存放集资项目的信息的实体类。
 * @author JuFF_白羽
 * @date 2018年7月6日 上午10:51:05
 */
@Data
@Entity
@Table(name = "T_MODIAN_POOL_PROJECT")
public class MoDianPoolProject implements Serializable {

	private static final long serialVersionUID = -319579424802892218L;

	/**
	 * 项目ID
	 * <p>
	 * 摩点上的项目ID
	 */
	@Id
	@Column(name = "ID")
	private Long id;

	/**
	 * 项目名称
	 */
	@Column(name = "NAME", length = 200)
	private String name;

	/**
	 * 项目开始时间
	 */
	@Column(name = "START_TIME")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date startTime;

	/**
	 * 项目结束时间
	 */
	@Column(name = "END_TIME")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date endTime;

	/**
	 * 发起位置
	 */
	@Column(name = "CITY", length = 10)
	private String city;

	/**
	 * 目标金额(元)
	 */
	@Column(name = "INSTALL_MONEY")
	private Double installMoney;

	/**
	 * 已筹金额(元)
	 */
	@Column(name = "BACKER_MONEY")
	private Double backerMoney;

	/**
	 * 完成度(%)
	 */
	@Column(name = "RATE")
	private Double rate;

	/**
	 * 支持人数(个)
	 */
	@Column(name = "BACKER_COUNT")
	private Integer backerCount;

	/**
	 * 项目状态:众筹中;已结束
	 */
	@Column(name = "STATUS", length = 10)
	private String status;

	/**
	 * 评论请求ID(发送获取评论请求的参数)
	 */
	@Column(name = "POST_ID")
	private Long postId;

	public MoDianPoolProject() {
	}

	public MoDianPoolProject(Long id) {
		this.id = id;
	}

}
