package com.snh48.picq.entity.system;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.snh48.picq.annotation.OperationType;

import lombok.Data;

/**
 * 日志表实体
 * 
 * @author shiro
 *
 */
@Data
@Entity
@Table(name = "T_SYS_LOG")
public class SystemLog implements Serializable {

	private static final long serialVersionUID = -5480371056078902409L;

	/** 自增主键 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private Long id;

	/** 用户ID */
	@Column(name = "USER_ID", updatable = false)
	private Long uid;

	/** 请求来源IP */
	@Column(name = "IP", length = 20)
	private String ip;

	/** 操作说明 */
	@Column(name = "OPER_DESC", length = 120)
	private String operDesc;

	/** 操作类型 */
	@Enumerated(EnumType.STRING)
	@Column(name = "OPER_TYPE", length = 10)
	private OperationType operType;

	/** 参数 */
	@Column(name = "PARAM", columnDefinition = "text")
	private String param;

	/**
	 * 操作是否成功状态
	 */
	@Column(name = "OPER_SUCCESS")
	private Boolean operSuccess;

	/** 补充内容（存放异常信息） */
	@Column(name = "SUPP_CONTENT", columnDefinition = "text")
	private String suppContent;

	/** 创建时间 */
	@Column(name = "CREATE_TIME")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date createTime;

}
