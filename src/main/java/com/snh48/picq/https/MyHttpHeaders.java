package com.snh48.picq.https;

import org.springframework.http.HttpHeaders;

@SuppressWarnings("serial")
public class MyHttpHeaders extends HttpHeaders {

	/**
	 * Appinfo实例相关参数
	 */
	public static final String APPINFO = "appInfo";

	/**
	 * 认证凭证
	 */
	public static final String POCKET_TOKEN = "token";
	
	public static final String REQUESTED_WITH = "X-Requested-With";

	public static final String PA = "pa";

}
