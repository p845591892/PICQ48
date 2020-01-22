package com.snh48.picq.https;

import org.springframework.http.MediaType;

@SuppressWarnings("serial")
public class MyMediaType extends MediaType {

	/**
	 * User-Agent请求头(iPhone)
	 */
	public static final String USER_AGENT_IPHONE = "PocketFans201807/6.0.9 (iPad; iOS 13.3; Scale/2.00)";

	/**
	 * Appinfo实例相关参数
	 */
	public static final String APPINFO = "{\"vendor\":\"apple\",\"deviceId\":\"9C6385C2-C4F6-4840-B284-65AA9A1F2F44\",\"appVersion\":\"6.0.9\",\"appBuild\":\"191230\",\"osVersion\":\"13.3.0\",\"osType\":\"ios\",\"deviceName\":\"iPad Air 2\",\"os\":\"ios\"}";

	/**
	 * ACCEPT的safari浏览器值
	 */
	public static final String SAFARI_VALUE = "application/json, text/plain, */*";

	/**
	 * USER_AGENT的safari浏览器值
	 */
	public static final String SAFARI_USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_4) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/12.1 Safari/605.1.15";

	/**
	 * 
	 */
	public static final String X_REQUESTED_WITH = "XMLHttpRequest";

	public MyMediaType(String type, String subtype) {
		super(type, subtype);
	}

}
