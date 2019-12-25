package com.snh48.picq.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * 酷Q配置
 * 
 * @author shiro
 *
 */
@Data
@ConfigurationProperties(prefix = "kuq", ignoreUnknownFields = true)
public class KuqProperties {

	/** 是否启动酷Q的websocket，默认false */
	private boolean enabled = Boolean.FALSE;

	/** 机器人名字 */
	private String botName = "bot1";

	/** 发送URL(酷Q的URL) */
	private String postUrl = "127.0.0.1";

	/** 发送端口(酷Q的端口) */
	private int postPort = 31091;

	/** 接收端口(PICQ的端口) */
	private int socketPort = 31092;

	/** Logger日志路径 (设为空就不输出文件了) */
	private String logPath = "logs";

	/** Logger日志文件名 */
	private String logFileName = "PicqBotX-Log";

	/** 是否输出 Init 日志，默认true */
	private boolean logInit = Boolean.TRUE;

	/** 管理员QQ号(必须与登录酷Q的账号是QQ好友)，默认为开发者(847109667)。 */
	private long adminId = 847109667;
	
	/** 酷Q客户端根目录的绝对路径（主要是用于，在同一机器部署的PICQ服务与酷Q客户端时，存放语音文件） */
	private String homePath;

}
