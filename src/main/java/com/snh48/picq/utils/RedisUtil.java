package com.snh48.picq.utils;

import org.crazycake.shiro.exception.SerializationException;
import org.crazycake.shiro.serializer.ObjectSerializer;
import org.crazycake.shiro.serializer.RedisSerializer;
import org.crazycake.shiro.serializer.StringSerializer;

import lombok.extern.log4j.Log4j2;
import redis.clients.jedis.Jedis;

@Log4j2
public class RedisUtil {

	@SuppressWarnings("rawtypes")
	private static RedisSerializer keySerializer = new StringSerializer();
	@SuppressWarnings("rawtypes")
	private static RedisSerializer valueSerializer = new ObjectSerializer();

	private static Jedis getJedis() {
		return SpringUtil.getBean(Jedis.class);
	}

	/**
	 * 以键值对形式保存数据
	 * 
	 * @param keyStr   字符串形式的键
	 * @param valueObj 对象形式的值
	 * @return 状态码
	 */
	@SuppressWarnings("unchecked")
	public static String set(String keyStr, Object valueObj) {
		byte[] key = null;
		byte[] value = null;
		try {
			key = keySerializer.serialize(keyStr);
			value = valueSerializer.serialize(valueObj);
		} catch (SerializationException e) {
			log.error("序列化 key={} 的键值对发生异常。", keyStr);
			e.printStackTrace();
		}
		return getJedis().set(key, value);
	}

	/**
	 * 以键值对形式保存数据
	 * 
	 * @param keyStr   字符串形式的键
	 * @param valueObj 对象形式的值
	 * @param seconds  key的生存时间（秒）
	 * @return 状态码
	 */
	@SuppressWarnings("unchecked")
	public static String setex(String keyStr, Object valueObj, int seconds) {
		byte[] key = null;
		byte[] value = null;
		try {
			key = keySerializer.serialize(keyStr);
			value = valueSerializer.serialize(valueObj);
		} catch (SerializationException e) {
			log.error("序列化 key={} 的键值对发生异常。", keyStr);
			e.printStackTrace();
		}
		return getJedis().setex(key, seconds, value);
	}

	/**
	 * 判断键是否存在
	 * 
	 * @param keyStr 字符串形式的key
	 * @return 存在返回true，否则返回false
	 */
	@SuppressWarnings("unchecked")
	public static boolean exists(String keyStr) {
		byte[] key = null;
		try {
			key = keySerializer.serialize(keyStr);
		} catch (SerializationException e) {
			log.error("序列化 key={} 发生异常。", keyStr);
			e.printStackTrace();
		}
		return getJedis().exists(key);
	}

	/**
	 * 获取键对应的值
	 * 
	 * @param keyStr 字符串形式的key
	 * @return {@link Object}
	 */
	@SuppressWarnings("unchecked")
	public static Object get(String keyStr) {
		byte[] key = null;
		byte[] value = null;
		try {
			key = keySerializer.serialize(keyStr);
		} catch (SerializationException e) {
			log.error("序列化 key={} 发生异常。", keyStr);
			e.printStackTrace();
		}
		value = getJedis().get(key);
		try {
			return valueSerializer.deserialize(value);
		} catch (SerializationException e) {
			log.error("反序列化 key={} 的值发生异常。", keyStr);
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static byte[] serializeObj(Object obj) throws SerializationException {
		return valueSerializer.serialize(obj);
	}

	public static Object deserializeObj(byte[] bytes) throws SerializationException {
		return valueSerializer.deserialize(bytes);
	}

	@SuppressWarnings("unchecked")
	public static byte[] serializeStr(String str) throws SerializationException {
		return keySerializer.serialize(str);
	}

	public static String deserializeStr(byte[] bytes) throws SerializationException {
		return (String) keySerializer.deserialize(bytes);
	}

}
