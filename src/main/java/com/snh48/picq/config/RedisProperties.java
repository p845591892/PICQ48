package com.snh48.picq.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;
import redis.clients.jedis.Protocol;

@Data
@ConfigurationProperties(prefix = "redis", ignoreUnknownFields = true)
public class RedisProperties {
	
	/**
	 * redis服务URL，例：127.0.0.1:6379。
	 */
	private String url;
	
	/**
	 * redis服务登录密码。
	 */
	private String password;
	
	/**
	 * 连接到redis服务的时间，非过期时间，默认2000。
	 */
	private int timeout = Protocol.DEFAULT_TIMEOUT;

}
