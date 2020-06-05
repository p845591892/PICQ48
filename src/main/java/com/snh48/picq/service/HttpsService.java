package com.snh48.picq.service;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import org.springframework.boot.configurationprocessor.json.JSONException;

import com.snh48.picq.entity.weibo.WeiboUser;

/**
 * Https请求服务接口
 * 
 * @author shiro
 *
 */
public interface HttpsService {

	/**
	 * 根据容器ID发送请求获取微博用户实体信息
	 * 
	 * @param containerUserId 容器ID(用户)
	 * @return 微博用户
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 * @throws JSONException
	 */
	WeiboUser getWeiboUser(Long containerUserId)
			throws KeyManagementException, NoSuchAlgorithmException, IOException, JSONException;
	
	/**
	 * 获取成员口袋房间ID
	 * @param memberId 成员ID
	 * @return 口袋房间ID
	 */
	long getRoomId(Long memberId);

}
