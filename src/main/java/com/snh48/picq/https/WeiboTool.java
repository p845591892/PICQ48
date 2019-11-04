package com.snh48.picq.https;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.transaction.annotation.Transactional;

import com.snh48.picq.entity.weibo.Dynamic;
import com.snh48.picq.entity.weibo.WeiboUser;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Transactional
public class WeiboTool extends JsonPICQ48 {
	/**
	 * 获取微博用户的信息。通过Https的方式发送请求，获得json结果，解析后生成{@link WeiboUser}的对象。
	 * 
	 * @param containerUserId 容器ID(用户)
	 * @return {@link WeiboUser}对象
	 */
	public static WeiboUser getUser(Long containerUserId) {
		try {
			JSONObject userObj = jsonWeiboUser(containerUserId);
			WeiboUser weiboUser = new WeiboUser();
			weiboUser.setContainerUserId(containerUserId);
			setWeiboUser(weiboUser, userObj);
			return weiboUser;
		} catch (KeyManagementException | NoSuchAlgorithmException | IOException | JSONException e) {
			log.error("获取WeiboUser发生异常：{}", e.getMessage());
		}
		return null;
	}

	/**
	 * 获取微博用户的动态。通过Https的方式发送请求，获得json结果，解析后生成{@link Dynamic}的集合。
	 * 
	 * @param containerDynamicId 动态数据的关键字段
	 * @return {@link Dynamic}集合
	 */
	public static List<Dynamic> getDynamicList(Long containerDynamicId) {
		try {
			JSONObject dynamicArry = jsonWeiboDynamic(containerDynamicId);
			JSONObject data = dynamicArry.getJSONObject("data");
			JSONArray cards = data.getJSONArray("cards");
			List<Dynamic> dynamicList = new ArrayList<Dynamic>();
			for (int i = 0; i < cards.length(); i++) {
				JSONObject card = cards.getJSONObject(i);
				int cardType = card.getInt("card_type");
				if (cardType == 9) {// 当为本人发的微博时
					Dynamic dynamic = new Dynamic();
					JSONObject mblog = card.getJSONObject("mblog");
					setWeiboDynamic(dynamic, mblog);
					dynamicList.add(dynamic);
				}
			}
			return dynamicList;
		} catch (KeyManagementException | NoSuchAlgorithmException | IOException | JSONException e) {
			log.error("获取List<Dynamic>发生异常：{}", e.getMessage());
		}
		return null;
	}

}
