package com.snh48.picq.utils;

import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.exception.SerializationException;
import org.crazycake.shiro.serializer.ObjectSerializer;
import org.crazycake.shiro.serializer.RedisSerializer;
import org.crazycake.shiro.serializer.StringSerializer;

import lombok.extern.log4j.Log4j2;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Log4j2
public class RedisUtil {

	private static RedisSerializer<String> keySerializer = new StringSerializer();
	private static RedisSerializer<Object> valueSerializer = new ObjectSerializer();

	private static JedisPool getJedisPool() {
		RedisManager redisManager = SpringUtil.getBean(RedisManager.class);
		if (redisManager.getJedisPool() == null) {
			String[] hostAndPort = redisManager.getHost().split(":");
			JedisPool jedisPool = new JedisPool(redisManager.getJedisPoolConfig(), hostAndPort[0],
					Integer.parseInt(hostAndPort[1]), redisManager.getTimeout(), redisManager.getPassword(),
					redisManager.getDatabase());
			redisManager.setJedisPool(jedisPool);
			return jedisPool;
		} else {
			return redisManager.getJedisPool();
		}
	}

	/**
	 * 以键值对形式保存数据
	 * 
	 * @param keyStr   字符串形式的键
	 * @param valueObj 对象形式的值
	 * @return 状态码
	 */
	public static String set(String keyStr, Object valueObj) {
		byte[] key = null;
		byte[] value = null;
		try {
			key = keySerializer.serialize(keyStr);
			value = valueSerializer.serialize(valueObj);
		} catch (Exception e) {
			log.error("序列化失败，key={}, value={}，异常：{}", keyStr, valueObj, e.toString());
			return "error";
		}
		Jedis jedis = getJedisPool().getResource();
		try {
			return jedis.set(key, value);
		} finally {
			jedis.close();
		}
	}

	/**
	 * 以键值对形式保存数据
	 * 
	 * @param keyStr   字符串形式的键
	 * @param valueObj 对象形式的值
	 * @param seconds  key的生存时间（秒）
	 * @return 状态码
	 */
	public static String setex(String keyStr, Object valueObj, int seconds) {
		byte[] key = null;
		byte[] value = null;
		try {
			key = keySerializer.serialize(keyStr);
			value = valueSerializer.serialize(valueObj);
		} catch (Exception e) {
			log.error("序列化失败，key={}, value={}, seconds={}，异常：{}", keyStr, valueObj, seconds, e.toString());
			return "error";
		}
		Jedis jedis = getJedisPool().getResource();
		try {
			return jedis.setex(key, seconds, value);
		} finally {
			jedis.close();
		}
	}

	/**
	 * 判断键是否存在
	 * 
	 * @param keyStr 字符串形式的key
	 * @return 存在返回true，否则返回false
	 */
	public static boolean exists(String keyStr) {
		byte[] key = null;
		try {
			key = keySerializer.serialize(keyStr);
		} catch (Exception e) {
			log.error("序列化失败，key={}，异常：{}", keyStr, e.toString());
		}
		Jedis jedis = getJedisPool().getResource();
		try {
			return jedis.exists(key);
		} finally {
			jedis.close();
		}
	}

	/**
	 * 获取键对应的值
	 * 
	 * @param keyStr 字符串形式的key
	 * @return {@link Object}
	 */
	public static Object get(String keyStr) {
		byte[] key = null;
		byte[] value = null;
		try {
			key = keySerializer.serialize(keyStr);
		} catch (Exception e) {
			log.error("序列化失败，key={}，异常：{}", keyStr, e.toString());
		}
		Jedis jedis = getJedisPool().getResource();
		try {
			value = jedis.get(key);
			return valueSerializer.deserialize(value);
		} catch (Exception e) {
			log.error("反序列化失败，value={}，异常：{}", keyStr, e.toString());
		} finally {
			jedis.close();
		}
		return null;
	}

	public static byte[] serializeObj(Object obj) throws SerializationException {
		return valueSerializer.serialize(obj);
	}

	public static Object deserializeObj(byte[] bytes) throws SerializationException {
		return valueSerializer.deserialize(bytes);
	}

	public static byte[] serializeStr(String str) throws SerializationException {
		return keySerializer.serialize(str);
	}

	public static String deserializeStr(byte[] bytes) throws SerializationException {
		return (String) keySerializer.deserialize(bytes);
	}

}
