package com.snh48.picq.https;

import org.springframework.http.MediaType;

public class MyMediaType extends MediaType {
	
	private static final long serialVersionUID = -5163892647113387830L;

	public MyMediaType(String type, String subtype) {
		super(type, subtype);
	}

	/**
	 * User-Agent请求头(iPhone)
	 */
	public static final String USER_AGENT_IPAD = "PocketFans201807/6.0.13 (iPad; iOS 13.5; Scale/2.00)";

	/**
	 * Appinfo实例相关参数
	 */
	public static final String APPINFO = "{\"vendor\":\"apple\",\"deviceId\":\"9C6385C2-C4F6-4840-B284-65AA9A1F2F44\",\"appVersion\":\"6.0.13\",\"appBuild\":\"200513\",\"osVersion\":\"13.5.0\",\"osType\":\"ios\",\"deviceName\":\"iPad Air 2\",\"os\":\"ios\"}";

	/**
	 * ACCEPT的safari浏览器值
	 */
	public static final String SAFARI_VALUE = "application/json, text/plain, */*";

	/**
	 * ACCEPT的chrome浏览器值
	 */
	public static final String CHROME_VALUE = "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9";

	/**
	 * USER_AGENT的safari浏览器值
	 */
	public static final String SAFARI_USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_4) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/12.1 Safari/605.1.15";

	/**
	 * USER_AGENT的chrome浏览器值
	 */
	public static final String CHROME_USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36";

	/**
	 * X请求
	 */
	public static final String X_REQUESTED_WITH = "XMLHttpRequest";

}
