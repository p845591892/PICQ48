package com.snh48.picq.entity.taoba;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 桃叭监控配置表
 * 
 * @author shiro
 *
 */
@Data
@Entity
@Table(name = "TAOBA_COMMUNITY_MONITOR")
public class TaobaMonitor implements Serializable {

	private static final long serialVersionUID = 7653820413629275327L;

	/**
	 * 自增序列
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private Long id;

	/** 桃叭项目ID */
	@Column(name = "DETAIL_ID")
	private Long detailId;

	/**
	 * 序列(QQ群号)
	 */
	@Column(name = "COMMUNITY_ID")
	private Long communityId;

}
