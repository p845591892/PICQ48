package com.snh48.picq.service.impl;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snh48.picq.core.Common.ExpireTime;
import com.snh48.picq.core.Common.RedisKey;
import com.snh48.picq.entity.weibo.WeiboUser;
import com.snh48.picq.https.JsonPICQ48;
import com.snh48.picq.service.HttpsService;
import com.snh48.picq.utils.Https;
import com.snh48.picq.utils.RedisUtil;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@Transactional
public class HttpsServiceImpl implements HttpsService {

	@Override
	public WeiboUser getWeiboUser(Long containerUserId) throws KeyManagementException, NoSuchAlgorithmException, IOException, JSONException {
		Https https = new Https();
		Map<String, String> params = new HashMap<String, String>();
		params.put("containerid", String.valueOf(containerUserId));

		Map<String, String> requestPropertys = new HashMap<String, String>();
		requestPropertys.put("Accept", "application/json, text/plain, */*");
		requestPropertys.put("User-Agent",
				"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_4) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/12.1 Safari/605.1.15");
		requestPropertys.put("X-Requested-With", "XMLHttpRequest");

		String result = https.setDataType("GET").setUrl("https://m.weibo.cn/api/container/getIndex").setParams(params)
				.setRequestProperty(requestPropertys).send();
		JSONObject jsonObject = new JSONObject(result);
		
		if (jsonObject.getInt("ok") == 1) {// 成功返回结果
			JSONObject data = jsonObject.getJSONObject("data");
			JSONObject userInfo = data.getJSONObject("userInfo");
			JSONObject tabsInfo = data.getJSONObject("tabsInfo");
			
			WeiboUser weiboUser = new WeiboUser();
			weiboUser.setAvatarHd(userInfo.getString("avatar_hd"));
			weiboUser.setContainerUserId(containerUserId);
			weiboUser.setContainerDynamicId(tabsInfo.getJSONArray("tabs").getJSONObject(1).getLong("containerid"));
			weiboUser.setFollowCount(userInfo.getInt("follow_count"));
			weiboUser.setFollowersCount(userInfo.getInt("followers_count"));
			weiboUser.setId(userInfo.getLong("id"));
			weiboUser.setUserName(userInfo.getString("screen_name"));
			
			return weiboUser;
			
		}
		return null;
	}

	@Override
	public long getRoomId(Long memberId) {
		Long roomId = (Long) RedisUtil.get(RedisKey.CONVERSATION_ + memberId);
		if (null != roomId) {
			return roomId;
		}
		
		try {
			JSONObject json = JsonPICQ48.jsonConversation(0);
			JSONArray array = json.getJSONArray("conversations");
			
			for (int i = 0; i < array.length(); i++) {
				JSONObject conversation = array.getJSONObject(i);
				long ownerId = conversation.getLong("ownerId");
				long targetId = conversation.getLong("targetId");
				if (ownerId == memberId) {
					roomId = targetId;
				}
				
				RedisUtil.setex(RedisKey.CONVERSATION_ + ownerId, targetId, ExpireTime.MONTH);
			}
			
			if (null == roomId) {
				roomId = 0l;
			}
		} catch (Exception e) {
			log.error("获取关注成员口袋房间ID错误，异常：{}", e.getMessage());
			roomId = -1l;
		}
		return roomId;
	}

}
