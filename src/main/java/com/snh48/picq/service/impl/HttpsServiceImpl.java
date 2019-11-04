package com.snh48.picq.service.impl;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snh48.picq.entity.weibo.WeiboUser;
import com.snh48.picq.service.HttpsService;
import com.snh48.picq.utils.Https;

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

}
