package com.snh48.picq.https;

import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

/**
 * Json处理的类，提供了请求获取的json字符串结果的处理函数。
 * 
 * @author shiro
 *
 */
public class JsonProcess {

	/**
	 * 将json字符串转换成{@link JSONObject}对象。
	 * 
	 * @param jsonStr json字符串
	 * @return object对象
	 * @throws JSONException
	 */
	public static JSONObject getJSONObjectByString(String jsonStr) throws JSONException {
		JSONObject jsonObject = new JSONObject(jsonStr);
		return jsonObject;
	}

	/**
	 * 将json字符串转换成{@link JSONArray}对象。
	 * 
	 * @param jsonStr json字符串
	 * @return array对象
	 * @throws JSONException
	 */
	public static JSONArray convertArrayByString(String jsonStr) throws JSONException {
		JSONArray jsonObject = new JSONArray(jsonStr);
		return jsonObject;
	}

}