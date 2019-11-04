package com.snh48.picq.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * 口袋48参数配置
 * @author shiro
 *
 */
@Data
@ConfigurationProperties(prefix = "pocket48", ignoreUnknownFields = true)
public class Pocket48Properties {
	
	/**
	 * 口袋账号
	 */
	private String username;
	
	/**
	 * 口袋密码
	 */
	private String password;

}
