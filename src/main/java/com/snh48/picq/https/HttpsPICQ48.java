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
import com.snh48.picq.core.Common.ExpireTime;
import com.snh48.picq.core.Common.RedisKey;
import com.snh48.picq.utils.Https;
import com.snh48.picq.utils.RedisUtil;
import com.snh48.picq.utils.SpringUtil;
import com.snh48.picq.utils.StringUtil;

/**
 * 口袋48的Https请求操作相关的类。该类提供Https请求的函数，来获取SNH48 Group相关的一些数据。
 * 
 * @author shiro
 *
 */
public abstract class HttpsPICQ48 {

	/**
	 * 发送Https请求，获取SNH48 Group全体成员列表。（V1版接口）
	 * 
	 * @return 全体成员列表的json字符串
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */
	public static String httpsAllMember() throws KeyManagementException, NoSuchAlgorithmException, IOException {
		Https https = new Https();
		String result = https.setDataType(HttpMethod.GET.name())
											.setUrl(HttpsURL.ALL_MEMBER_LIST_V1)
											.send();
		return result;
	}

	/**
	 * 发送Https请求，获取SNH48 Group全体成员列表。（V2版接口）
	 * <p>
	 * 该版本添加了可分别团体查询的参数 gid。全体 gid=00，SNH48 gid=10， BEJ48 gid=11，GNZ48 gid=12...
	 * </p>
	 * 
	 * @param gid 团体ID，建议参数00
	 * @return 成员列表字符串
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 */
	public static String httpsAllMemberV2(String gid)
			throws KeyManagementException, NoSuchAlgorithmException, IOException {
		Https https = new Https();
		/* 请求头 */
		Map<String, String> requestPropertys = new HashMap<String, String>();
		requestPropertys.put(MyHttpHeaders.ACCEPT, MyMediaType.CHROME_VALUE);
		requestPropertys.put(MyHttpHeaders.USER_AGENT, MyMediaType.CHROME_USER_AGENT);
		/* 请求参数 */
		Map<String, String> params = new HashMap<String, String>();
		params.put("gid", gid);
//		params.put("callback", "get_members_success");
//		params.put("_", String.valueOf(System.currentTimeMillis()));
		/* 发送请求 */
		String result = https.setDataType(HttpMethod.GET.name())
											.setRequestProperty(requestPropertys)
											.setParams(params)
											.setUrl(HttpsURL.ALL_MEMBER_LIST_V2)
											.send();
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
	public static String httpsMember(long memberId)
			throws KeyManagementException, NoSuchAlgorithmException, IOException, JSONException {
		Https https = new Https();
		/* 请求头 */
		Map<String, String> requestPropertys = new HashMap<String, String>();
		requestPropertys.put(MyHttpHeaders.ACCEPT, MyMediaType.ALL_VALUE);
		requestPropertys.put(MyHttpHeaders.CONTENT_TYPE, MyMediaType.APPLICATION_JSON_VALUE);
		requestPropertys.put(MyHttpHeaders.USER_AGENT, MyMediaType.USER_AGENT_IPAD);
		/* 请求参数 */
		String payloadJson = "{\"memberId\":\"" + String.valueOf(memberId) + "\"}";
		/* 发送请求 */
		String memberJson = https.setUrl(HttpsURL.MEMBER)
													.setDataType(HttpMethod.POST.name())
													.setRequestProperty(requestPropertys)
													.setPayloadJson(payloadJson)
													.send();
		return memberJson;
	}

	/**
	 * 发送Https请求，获取SNH48成员的个人房间信息。
	 * 
	 * @param roomId     房间ID
	 * @param targetType 请求类型(默认填0，暂不推荐其他参数。)
	 * @return 成员个人房间信息的json字符串
	 * @throws JSONException
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */
	public static String httpsMemberRoom(long roomId, int targetType)
			throws KeyManagementException, NoSuchAlgorithmException, IOException, JSONException {
		/* 请求参数 */
		String payloadJson = "{\"roomId\":\"" + roomId + "\",\"targetType\":" + targetType + "}";
		return httpsPocket(payloadJson, HttpsURL.MEMBER_ROOM);
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
	protected static String getToken()
			throws KeyManagementException, NoSuchAlgorithmException, IOException, JSONException {
		String token = "";
		if (RedisUtil.exists(RedisKey.TOKEN)) {
			token = (String) RedisUtil.get(RedisKey.TOKEN);
		} else {
			token = refreshToken();
		}
		return token;
	}
	
	public static long getUserId() throws KeyManagementException, NoSuchAlgorithmException, IOException, JSONException {
		long userId = 0;
		if (RedisUtil.exists(RedisKey.USER_ID)) {
			userId = (long) RedisUtil.get(RedisKey.USER_ID);
		} else {
			refreshToken();
			userId = (long) RedisUtil.get(RedisKey.USER_ID);
		}
		return userId;
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
		long userId = loginObj.getJSONObject("content").getJSONObject("userInfo").getLong("userId");
		RedisUtil.setex(RedisKey.TOKEN, token, ExpireTime.WEEK);
		RedisUtil.setex(RedisKey.USER_ID, userId, ExpireTime.WEEK);
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
		requestPropertys.put(MyHttpHeaders.USER_AGENT, MyMediaType.USER_AGENT_IPAD);
		requestPropertys.put(MyHttpHeaders.APPINFO, MyMediaType.APPINFO);
		/* 请求参数 */
		String payloadJson = "{\"mobile\":\"" + username + "\",\"pwd\":\"" + password + "\"}";
		/* 发送请求 */
		String loginJson = https.setUrl(HttpsURL.TOKEN)
												.setDataType(HttpMethod.POST.name())
												.setPayloadJson(payloadJson)
												.setRequestProperty(requestPropertys)
												.send();
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
	public static String httpsRoomMessage(String memberId, String roomId, long nextTime)
			throws KeyManagementException, NoSuchAlgorithmException, IOException, JSONException {
		/* 请求参数 */
		String payloadJson = "{\"needTop1Msg\":false,\"roomId\":\"" + roomId + "\",\"ownerId\":\"" + memberId
				+ "\",\"nextTime\":" + nextTime + "}";
		return httpsPocket(payloadJson, HttpsURL.ROOM_MESSAGE);
	}

	/**
	 * 发送Https请求，获取口袋48成员房间的留言板消息列表。
	 * <p>
	 * 参数详细说明：
	 * 
	 * <pre>
	 * {@link nextTime}：下条消息的时间戳，默认当前页时参数为0，即最新消息。
	 * {@link needTop1Msg}：是否需要最新一条数据，当为true且{@link nextTime}为0时返回第一页，为false即为向上翻页，时间戳{@link nextTime}也从0开始。
	 * {@link roomId}：口袋房间ID，用于指定查找的房间。
	 * </pre>
	 * 
	 * @param nextTime    下条消息的时间戳。
	 * @param needTop1Msg 是否需要最新一条数据。
	 * @param roomId      口袋房间ID。
	 * @return 房间留言板消息的json字符串。
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 * @throws JSONException
	 */
	public static String httpsRoomMessageALL(long nextTime, boolean needTop1Msg, long roomId)
			throws KeyManagementException, NoSuchAlgorithmException, IOException, JSONException {
		/* 请求参数 */
		String payloadJson = "{\"nextTime\":" + String.valueOf(nextTime) + ",\"needTop1Msg\":"
				+ String.valueOf(needTop1Msg) + ",\"roomId\":\"" + roomId + "\"}";
		return httpsPocket(payloadJson, HttpsURL.ROOM_MESSAGE_ALL);
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
	public static String httpsFlipcardContent(String questionId, String answerId)
			throws KeyManagementException, NoSuchAlgorithmException, IOException, JSONException {
		/* 请求参数 */
		String payloadJson = "{\"questionId\":\"" + questionId + "\",\"answerId\":\"" + answerId + "\"}";
		return httpsPocket(payloadJson, HttpsURL.ROOM_MESSAGE_FLIPCARD);
	}

	/**
	 * 发送HTTPS请求，获取口袋房间的行程列表。
	 * <p>
	 * 参数详细说明：
	 * 
	 * <pre>
	 * {@link lastTime}：时间戳，默认当前页时参数为0，即不会查询出历史行程。
	 * {@link groupId}：团体ID，例如SNH8、GNZ48，默认全团时参数为0，即查询整个48GROUP的行程。
	 * {@link isMore}：是否更多，当为true时，配合{@link lastTime}，将向下翻页，即查询更未来的行程，否则相反。
	 * </pre>
	 * 
	 * @param lastTime 时间戳
	 * @param groupId  团体ID
	 * @param isMore   是否更多
	 * @param userId   当前登录的用户ID
	 * @return 返回行程列表的JSON字符串。
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 * @throws JSONException 
	 */
	public static String httpsTrip(long lastTime, int groupId, boolean isMore, long userId)
			throws KeyManagementException, NoSuchAlgorithmException, IOException, JSONException {
		/* 请求参数 */
		String payloadJson = "{\"groupId\":" + groupId + ",\"lastTime\":\"" + lastTime + "\",\"isMore\":" + isMore
				+ ",\"userId\":" + userId + "}";
		return httpsPocket(payloadJson, HttpsURL.TRIP);
	}

	/**
	 * 发送HTTPS请求，获取口袋48的用户（部分）信息。
	 * 
	 * @param needMuteInfo
	 * @param userId       用户ID
	 * @return 返回用户（部分）信息的json字符串。
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 * @throws JSONException
	 */
	public static String httpsPocketUser(int needMuteInfo, long userId)
			throws KeyManagementException, NoSuchAlgorithmException, IOException, JSONException {
		/* 请求参数 */
		String payloadJson = "{\"needMuteInfo\":" + String.valueOf(needMuteInfo) + ",\"userId\":\""
				+ String.valueOf(userId) + "\"}";
		return httpsPocket(payloadJson, HttpsURL.USER_SMALL);
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
		String jsonStr = https.setDataType(HttpMethod.GET.name())
											.setUrl(HttpsURL.WEIBO)
											.setParams(params)
											.setRequestProperty(requestPropertys)
											.send();
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
	
	/**
	 * 发送Https请求，获取口袋48关注的成员房间列表。
	 * 
	 * @param targetType 请求类型(默认填0，暂不推荐其他参数。)
	 * @return 关注的口袋房间列表json字符串。
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 * @throws JSONException
	 */
	public static String httpsConversation(int targetType)
			throws KeyManagementException, NoSuchAlgorithmException, IOException, JSONException {
		/* 请求参数 */
		String payloadJson = "{\"targetType\":" + targetType + "}";
		return httpsPocket(payloadJson, HttpsURL.CONVERSATION);
	}
	
	private static String httpsPocket(String payloadJson, String url) 
			throws KeyManagementException, NoSuchAlgorithmException, IOException, JSONException {
		Https https = new Https();
		/* 请求头 */
		Map<String, String> requestPropertys = new HashMap<String, String>();
		requestPropertys.put(MyHttpHeaders.ACCEPT, MyMediaType.ALL_VALUE);
		requestPropertys.put(MyHttpHeaders.CONTENT_TYPE, MyMediaType.APPLICATION_JSON_VALUE);
		requestPropertys.put(MyHttpHeaders.USER_AGENT, MyMediaType.USER_AGENT_IPAD);
		requestPropertys.put(MyHttpHeaders.APPINFO, MyMediaType.APPINFO);
		requestPropertys.put(MyHttpHeaders.PA, StringUtil.getPa());
		requestPropertys.put(MyHttpHeaders.POCKET_TOKEN, getToken());
		/* 发送请求 */
		String jsonStr = https.setDataType(HttpMethod.POST.name())
												.setPayloadJson(payloadJson)
												.setRequestProperty(requestPropertys)
												.setUrl(url)
												.send();
		return jsonStr;
	}
	
	/**
	 * 发送Https请求，获取桃叭项目信息。
	 * 
	 * @param id 桃叭项目ID
	 * @return 桃叭项目信息的json字符串
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 */
	public static String httpsTaobaDetail(long id)
			throws KeyManagementException, NoSuchAlgorithmException, IOException {
		/* 请求参数 */
		long requestTime = System.currentTimeMillis() / 1000;
		String payloadJson = "{\"id\":\"" + id + "\",\"requestTime\":" + requestTime + "000,\"pf\":\"h5\"}";
		return httpsTaoba(payloadJson, HttpsURL.TAOBA_DETAIL);
	}
	
	/**
	 * 发送Https请求，获取桃叭集资详细列表。
	 * 
	 * @param ismore 是否更多
	 * @param limit  查询条数
	 * @param id     项目ID
	 * @param offset 已翻过条数
	 * @return 桃叭项目详细列表的json字符串
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 */
	public static String httpsTaobaJoin(boolean ismore, int limit, long id, int offset)
			throws KeyManagementException, NoSuchAlgorithmException, IOException {
		/* 请求参数 */
		long requestTime = System.currentTimeMillis();
		String payloadJson = "{\"ismore\":" + ismore + ",\"limit\":" + limit + ",\"id\":\"" + id + "\",\"offset\":"
				+ offset + ",\"requestTime\":" + requestTime + ",\"_version_\":1,\"pf\":\"h5\"}";
		return httpsTaoba(payloadJson, HttpsURL.TAOBA_JOIN);
	}
	
	/**
	 * 发送Https请求，获取桃叭集资排名列表。
	 * 
	 * @param id 项目ID
	 * @return 叭项目排名列表的json字符串
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 */
	public static String httpsTaobaRank(long id) 
			throws KeyManagementException, NoSuchAlgorithmException, IOException {
		/* 请求参数 */
		long requestTime = System.currentTimeMillis();
		String payloadJson = "{\"id\":\"" + id + "\",\"requestTime\":" + requestTime
				+ ",\"_version_\":1,\"pf\":\"h5\"}";
		return httpsTaoba(payloadJson, HttpsURL.TAOBA_RANK);
	}
	
	private static String httpsTaoba(String payloadJson, String url) 
			throws KeyManagementException, NoSuchAlgorithmException, IOException {
		Https https = new Https();
		/* 请求头 */
		Map<String, String> requestPropertys = new HashMap<String, String>();
		requestPropertys.put(MyHttpHeaders.ACCEPT, MyMediaType.SAFARI_VALUE);
		requestPropertys.put(MyHttpHeaders.USER_AGENT, MyMediaType.SAFARI_USER_AGENT);
		requestPropertys.put(MyHttpHeaders.CONTENT_TYPE, MyMediaType.APPLICATION_JSON_VALUE);
		/* 请求参数 */
		payloadJson = StringUtil.encodePayloadJson(payloadJson);
		/* 发送请求 */
		String resultStr = https.setDataType(HttpMethod.POST.name())
											.setUrl(url)
											.setPayloadJson(payloadJson)
											.setRequestProperty(requestPropertys)
											.send();
		return StringUtil.decodeRequestResult(resultStr);
	}
}
