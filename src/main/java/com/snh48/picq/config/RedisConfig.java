package com.snh48.picq.config;

import org.crazycake.shiro.RedisManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.snh48.picq.utils.StringUtil;

/**
 * redis配置类
 * 
 * @author shiro
 *
 */
@Configuration
public class RedisConfig {

	@Autowired
	private RedisProperties properties;

	/**
	 * redis管理器Bean
	 */
	@Bean
	public RedisManager redisManager() {
		RedisManager redisManager = new RedisManager();
		if (StringUtil.isNotBlank(properties.getUrl())) {
			redisManager.setHost(properties.getUrl());
		}
		if (StringUtil.isNotBlank(properties.getPassword())) {
			redisManager.setPassword(properties.getPassword());
		}
		redisManager.setTimeout(properties.getTimeout());
		return redisManager;
	}

}