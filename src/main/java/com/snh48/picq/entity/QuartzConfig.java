package com.snh48.picq.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * @ClassName: QuartzConfig
 * @Description: 定时任务配置实体类
 * @author JuFF_白羽
 * @date 2018年3月16日 下午2:58:19
 */
@Data
@Entity
@Table(name = "T_CONFIG_QUARTZ")
public class QuartzConfig implements Serializable {

	private static final long serialVersionUID = -4358118096158148570L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "JOB_NAME", length = 50)
	private String jobName;

	@Column(name = "CRON_TRIGGER", length = 50)
	private String cronTrigger;

	@Column(name = "CRON", length = 20)
	private String cron;
	
	/**
     * 状态:0未启动false/1启动true
     */
	@Column(name = "STATUS")
    private Boolean status;
	
	@Column(name = "CLASS_PATH", length = 200)
	private String classPath;
	
	@Column(name = "JOB_DESC", length = 200)
	private String jobDesc;
	
}
