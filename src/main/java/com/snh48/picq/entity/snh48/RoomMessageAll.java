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
 * 口袋房间留言板消息表
 * 
 * @author shiro
 *
 */
@Data
@Entity
@Table(name = "POCKET_ROOM_MESSAGE_ALL")
public class RoomMessageAll implements Serializable {

	private static final long serialVersionUID = 180878167764218327L;

	/**
	 * 消息ID，对应msgidClient字段
	 */
	@Id
	@Column(name = "ID", length = 200)
	private String id;

	/**
	 * 发送消息时间
	 */
	@Column(name = "MESSAGE_TIME")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date messageTime;

	/**
	 * 发送人ID
	 */
	@Column(name = "SENDER_USER_ID")
	private Long senderUserId;

	/**
	 * 所属房间ID
	 */
	@Column(name = "ROOM_ID")
	private Long roomId;

	/**
	 * 消息类型
	 */
	@Column(name = "MESSAGE_TYPE", length = 20)
	private String messageType;

	/**
	 * 回复的消息ID
	 */
	@Column(name = "REPLY_ID", length = 200)
	private String replyMessageId;
	
	/**
	 * 被回复人昵称
	 */
	@Column(name = "REPLY_NAME", length = 100)
	private String replyName;

	/**
	 * 消息内容
	 */
	@Column(name = "MESSAGE_CONTENT", columnDefinition = "text")
	private String messageContent;
	
}
