package com.snh48.picq.https;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpMethod;

import com.snh48.picq.config.Pocket48Properties;
import com.snh48.picq.utils.Https;
import com.snh48.picq.utils.RedisUtil;
import com.snh48.picq.utils.SpringUtil;

/**
 * 口袋48的Https请求操作相关的类。该类提供Https请求的函数，来获取SNH48 Group相关的一些数据。
 * 
 * @author shiro
 *
 */
public abstract class HttpsPICQ48 implements PICQ48 {

	/**
	 * redis存储token的key。
	 */
	public static final String TOKEN_KEY = "pocket_token";

	/**
	 * redis存储token的过期时间。单位：秒。
	 */
	public static final int EXPIRE_TIME = 60 * 60 * 24 * 30;

	/**
	 * 发送Https请求，获取SNH48 Group全体成员列表。
	 * 
	 * @return 全体成员列表的json字符串
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */
	public static String httpsAllMember() throws KeyManagementException, NoSuchAlgorithmException, IOException {
		Https https = new Https();
		String result = https.setDataType(HttpMethod.GET.name()).setUrl(HttpsURL.ALL_MEMBER_LIST).send();
		return result;
	}

	/**
	 * 发送Https请求，获取SNH48成员的个人详细信息。
	 * 
	 * @param memberId 成员ID
	 * @return 成员个人信息的json字符串
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 * @throws JSONException
	 */
	@SuppressWarnings("deprecation")
	public static String httpsMember(long memberId)
			throws KeyManagementException, NoSuchAlgorithmException, IOException, JSONException {
		Https https = new Https();
		/* 请求头 */
		Map<String, String> requestPropertys = new HashMap<String, String>();
		requestPropertys.put(MyHttpHeaders.ACCEPT, MyMediaType.ALL_VALUE);
		requestPropertys.put(MyHttpHeaders.CONTENT_TYPE, MyMediaType.APPLICATION_JSON_UTF8_VALUE);
		requestPropertys.put(MyHttpHeaders.USER_AGENT, MyMediaType.USER_AGENT_IPHONE);
		/* 请求参数 */
		String payloadJson = "{\"memberId\":\"" + String.valueOf(memberId) + "\"}";
		/* 发送请求 */
		String memberJson = https.setUrl(HttpsURL.MEMBER).setDataType(HttpMethod.POST.name())
				.setRequestProperty(requestPropertys).setPayloadJson(payloadJson).send();
		return memberJson;
	}

	/**
	 * 发送Https请求，获取SNH48成员的个人房间信息。
	 * 
	 * @param sourceId 成员ID，对应memberId
	 * @param type     请求类型(默认填0，暂不推荐其他参数。)
	 * @return 成员个人房间信息的json字符串
	 * @throws JSONException
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */
	@SuppressWarnings("deprecation")
	public static String httpsMemberRoom(long sourceId, int type)
			throws KeyManagementException, NoSuchAlgorithmException, IOException, JSONException {
		Https https = new Https();
		/* 请求头 */
		Map<String, String> requestPropertys = new HashMap<String, String>();
		requestPropertys.put(MyHttpHeaders.ACCEPT, MyMediaType.ALL_VALUE);
		requestPropertys.put(MyHttpHeaders.CONTENT_TYPE, MyMediaType.APPLICATION_JSON_UTF8_VALUE);
		requestPropertys.put(MyHttpHeaders.USER_AGENT, MyMediaType.USER_AGENT_IPHONE);
		requestPropertys.put(MyHttpHeaders.APPINFO, MyMediaType.APPINFO);
		requestPropertys.put(MyHttpHeaders.POCKET_TOKEN, getToken());
		/* 请求参数 */
		String payloadJson = "{\"sourceId\":\"" + String.valueOf(sourceId) + "\",\"type\":\"" + String.valueOf(type)
				+ "\"}";
		/* 发送请求 */
		String roomJson = https.setUrl(HttpsURL.MEMBER_ROOM).setDataType(HttpMethod.POST.name())
				.setRequestProperty(requestPropertys).setPayloadJson(payloadJson).send();
		return roomJson;
	}

	/**
	 * 获取口袋48认证凭据，即登录口袋48后要设置在请求头中的Token。
	 * 
	 * @return 口袋48认证凭据
	 * @throws JSONException
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */
	public static String getToken()
			throws KeyManagementException, NoSuchAlgorithmException, IOException, JSONException {
		String token = "";
		if (RedisUtil.exists(TOKEN_KEY)) {
			token = (String) RedisUtil.get(TOKEN_KEY);
		} else {
			token = refreshToken();
		}
		return token;
	}

	/**
	 * 刷新redis中存储的token
	 * 
	 * @return 刷新后的token
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 * @throws JSONException
	 */
	protected static String refreshToken()
			throws KeyManagementException, NoSuchAlgorithmException, IOException, JSONException {
		Pocket48Properties properties = SpringUtil.getBean(Pocket48Properties.class);
		String jsonStr = httpsToken(properties.getUsername(), properties.getPassword());
		JSONObject loginObj = JsonProcess.getJSONObjectByString(jsonStr);
		String token = loginObj.getJSONObject("content").getJSONObject("userInfo").getString("token");
		RedisUtil.setex(TOKEN_KEY, token, EXPIRE_TIME);
		return token;
	}

	/**
	 * 发送Https请求，获取口袋48登录信息。
	 * 
	 * @param username 用户名
	 * @param password 密码
	 * @return 用户登录信息json字符串
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 */
	@SuppressWarnings("deprecation")
	protected static String httpsToken(String username, String password)
			throws KeyManagementException, NoSuchAlgorithmException, IOException {
		Https https = new Https();
		/* 请求头 */
		Map<String, String> requestPropertys = new HashMap<String, String>();
		requestPropertys.put(MyHttpHeaders.ACCEPT, MyMediaType.ALL_VALUE);
		requestPropertys.put(MyHttpHeaders.CONTENT_TYPE, MyMediaType.APPLICATION_JSON_UTF8_VALUE);
		requestPropertys.put(MyHttpHeaders.USER_AGENT, MyMediaType.USER_AGENT_IPHONE);
		requestPropertys.put(MyHttpHeaders.APPINFO, MyMediaType.APPINFO);
		/* 请求参数 */
		String payloadJson = "{\"mobile\":\"" + username + "\",\"pwd\":\"" + password + "\"}";
		/* 发送请求 */
		String loginJson = https.setUrl(HttpsURL.TOKEN).setDataType(HttpMethod.POST.name()).setPayloadJson(payloadJson)
				.setRequestProperty(requestPropertys).send();
		return loginJson;
	}

	/**
	 * 发送Https请求，获取口袋48成员房间的消息列表。
	 * 
	 * @param memberId 成员ID
	 * @param roomId   房间ID
	 * @param nextTime 下条消息的时间戳。要获取最新消息该参数为0
	 * @return 房间消息的json字符串
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 * @throws JSONException
	 */
	@SuppressWarnings("deprecation")
	public static String httpsRoomMessage(String memberId, String roomId, long nextTime)
			throws KeyManagementException, NoSuchAlgorithmException, IOException, JSONException {
		Https https = new Https();
		/* 请求头 */
		Map<String, String> requestPropertys = new HashMap<String, String>();
		requestPropertys.put(MyHttpHeaders.ACCEPT, MyMediaType.ALL_VALUE);
		requestPropertys.put(MyHttpHeaders.CONTENT_TYPE, MyMediaType.APPLICATION_JSON_UTF8_VALUE);
		requestPropertys.put(MyHttpHeaders.USER_AGENT, MyMediaType.USER_AGENT_IPHONE);
		requestPropertys.put(MyHttpHeaders.POCKET_TOKEN, getToken());
		/* 请求参数 */
		String payloadJson = "{\"ownerId\":\"" + memberId + "\",\"needTop1Msg\":\"false\",\"nextTime\":\""
				+ String.valueOf(nextTime) + "\",\"roomId\":\"" + roomId + "\"}";
		/* 发送请求 */
		String messageStr = https.setDataType(HttpMethod.POST.name()).setRequestProperty(requestPropertys)
				.setPayloadJson(payloadJson).setUrl(HttpsURL.ROOM_MESSAGE).send();
		return messageStr;
	}

	/**
	 * 发送Https请求，获取口袋房间翻牌详情信息。
	 * <p>
	 * 本请求返回值包括问题、回答、提问人昵称、回答人昵称（重要）等。
	 * 
	 * @param questionId 问题ID
	 * @param answerId   回答ID
	 * @return 翻牌信息的json字符串
	 * @throws JSONException
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */
	@SuppressWarnings("deprecation")
	public static String httpsFlipcardContent(String questionId, String answerId)
			throws KeyManagementException, NoSuchAlgorithmException, IOException, JSONException {
		Https https = new Https();
		/* 请求头 */
		Map<String, String> requestPropertys = new HashMap<String, String>();
		requestPropertys.put(MyHttpHeaders.ACCEPT, MyMediaType.ALL_VALUE);
		requestPropertys.put(MyHttpHeaders.CONTENT_TYPE, MyMediaType.APPLICATION_JSON_UTF8_VALUE);
		requestPropertys.put(MyHttpHeaders.USER_AGENT, MyMediaType.USER_AGENT_IPHONE);
		requestPropertys.put(MyHttpHeaders.POCKET_TOKEN, getToken());
		/* 请求参数 */
		String payloadJson = "{\"questionId\":\"" + questionId + "\",\"answerId\":\"" + answerId + "\"}";
		/* 发送请求 */
		String jsonStr = https.setDataType(HttpMethod.POST.name()).setRequestProperty(requestPropertys)
				.setPayloadJson(payloadJson).setUrl(HttpsURL.ROOM_MESSAGE_FLIPCARD).send();
		return jsonStr;
	}

	/**
	 * 自动补全无http或https的48资源地址
	 * 
	 * @param sourceUrl 原获取到的url
	 * @return 补全后的URL
	 */
	protected static String getSourceUrl(String sourceUrl) {
		if (!sourceUrl.startsWith("http://") && !sourceUrl.startsWith("https://")) {
			sourceUrl = HttpsURL.SOURCE + sourceUrl;
		}
		return sourceUrl;
	}

	/**
	 * 发送Https请求，获取微博用户信息。
	 * 
	 * @param containerUserId 容器ID(用户)
	 * @return 用户信息的json字符串
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 */
	public static String httpsWeiboUser(Long containerUserId)
			throws KeyManagementException, NoSuchAlgorithmException, IOException {
		Https https = new Https();
		/* 请求头 */
		Map<String, String> requestPropertys = new HashMap<String, String>();
		requestPropertys.put(MyHttpHeaders.ACCEPT, MyMediaType.SAFARI_VALUE);
		requestPropertys.put(MyHttpHeaders.USER_AGENT, MyMediaType.SAFARI_USER_AGENT);
		requestPropertys.put(MyHttpHeaders.REQUESTED_WITH, MyMediaType.X_REQUESTED_WITH);
		/* URL携带参数 */
		Map<String, String> params = new HashMap<String, String>();
		params.put("containerid", String.valueOf(containerUserId));
		/* 发送请求 */
		String jsonStr = https.setDataType(HttpMethod.GET.name()).setUrl(HttpsURL.WEIBO).setParams(params)
				.setRequestProperty(requestPropertys).send();
		return jsonStr;
	}

	/**
	 * 发送Https请求，获取微博用户的动态列表
	 * 
	 * @param containerDynamicId 动态数据的关键字段
	 * @return 动态的json字符串
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 */
	public static String httpsWeiboDynamic(Long containerDynamicId)
			throws KeyManagementException, NoSuchAlgorithmException, IOException {
		/* 两个接口只是参数不同 */
		String jsonStr = httpsWeiboUser(containerDynamicId);
		return jsonStr;
	}

}
