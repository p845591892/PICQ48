package com.snh48.picq.entity.weibo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * @ClassName: DynamicMonitor
 * @Description: 微博动态监控配置表
 *               <p>
 *               QQ群与微博动态监控关系中间表。
 * @author JuFF_白羽
 * @date 2018年8月1日 上午10:53:06
 */
@Data
@Entity
@Table(name = "T_MONITOR_WEIBO_COMMUNITY")
public class DynamicMonitor implements Serializable {

	private static final long serialVersionUID = 1408622814478870821L;

	/**
	 * 自增序列
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private Long id;

	/**
	 * 序列(QQ群号)
	 */
	@Column(name = "COMMUNITY_ID")
	private Long communityId;

	/**
	 * 微博用户ID
	 */
	@Column(name = "USER_ID")
	private Long userId;

}
