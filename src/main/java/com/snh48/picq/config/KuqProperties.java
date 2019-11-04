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

	/** 发送URL */
	private String postUrl = "127.0.0.1";

	/** 发送端口 */
	private int postPort = 31091;

	/** 接收端口 */
	private int socketPort = 31092;

	/** Logger日志路径 (设为空就不输出文件了) */
	private String logPath = "logs";

	/** Logger日志文件名 */
	private String logFileName = "PicqBotX-Log";

	/** 是否输出 Init 日志，默认true */
	private boolean logInit = Boolean.TRUE;

}
