package com.snh48.picq.entity.weibo;

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
 * @ClassName: Member
 * @Description: 微博动态表
 * @author JuFF_白羽
 * @date 2018年7月31日 下午5:11:46
 */
@Data
@Entity
@Table(name = "T_WEIBO_DYNAMIC")
public class Dynamic implements Serializable {

	private static final long serialVersionUID = 4091509184096204351L;

	/**
	 * 消息ID
	 */
	@Id
	@Column(name = "ID")
	private Long id;

	/**
	 * 消息内容: text类型
	 */
	@Column(name = "WEIBO_CONTENT", columnDefinition = "text")
	private String weiboContent;

	/**
	 * 发送人姓名
	 */
	@Column(name = "SENDER_NAME", length = 50)
	private String senderName;

	/**
	 * 发送人ID
	 */
	@Column(name = "USER_ID")
	private Long userId;

	/**
	 * 消息创建时间
	 */
	@Column(name = "CREATED_AT", length = 50)
	private String createdAt;

	/**
	 * 同步时间
	 */
	@Column(name = "SYNC_DATE")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date syncDate;

	/**
	 * 是否发送：1未发送，2已发送
	 * <p>
	 * 这里是指是否执行过发送任务，并不是说一定会发送到指定的QQ
	 */
	@Column(name = "IS_SEND")
	private Integer isSend;
	
	/**
	 * 是否为置顶微博
	 */
	@Column(name = "IS_TOP")
	private Boolean isTop;

}
