package com.snh48.picq.config;

import org.crazycake.shiro.RedisManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.snh48.picq.utils.StringUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

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

	/**
	 * 创建jedis的Bean
	 * 
	 * @param redisManager redis管理器
	 */
	@Bean
	@SuppressWarnings("resource")
	public Jedis jedis(RedisManager redisManager) {
		String[] hostAndPort = redisManager.getHost().split(":");
		JedisPool jedisPool = new JedisPool(redisManager.getJedisPoolConfig(), hostAndPort[0],
				Integer.parseInt(hostAndPort[1]), redisManager.getTimeout(), redisManager.getPassword(),
				redisManager.getDatabase());
		return jedisPool.getResource();
	}

}