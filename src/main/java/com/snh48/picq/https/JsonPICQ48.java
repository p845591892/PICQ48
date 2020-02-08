package com.snh48.picq.https;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import com.snh48.picq.entity.snh48.Member;
import com.snh48.picq.entity.snh48.PocketUser;
import com.snh48.picq.entity.snh48.RoomMessage;
import com.snh48.picq.entity.snh48.RoomMessageAll;
import com.snh48.picq.entity.snh48.Trip;
import com.snh48.picq.entity.weibo.Dynamic;
import com.snh48.picq.entity.weibo.WeiboUser;
import com.snh48.picq.exception.HttpsPocketAuthenticateException;
import com.snh48.picq.utils.DateUtil;

import lombok.extern.log4j.Log4j2;

/**
 * 口袋48的json操作抽象类。该类提供各种针对不同接口请求返回的json进行特殊处理的函数。
 * 
 * @author shiro
 *
 */
@Log4j2
public abstract class JsonPICQ48 extends HttpsPICQ48 {

	/**
	 * 发送Https请求，获取SNH48 Group全体成员列表。（V1版接口）
	 * 
	 * @return 全体成员列表的json对象
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 * @throws JSONException
	 */
	public static JSONObject jsonAllMember()
			throws KeyManagementException, NoSuchAlgorithmException, IOException, JSONException {
		String jsonStr = httpsAllMember();
		return JsonProcess.getJSONObjectByString(jsonStr);
	}

	/**
	 * 发送Https请求，获取SNH48 Group全体成员列表。（V2版接口）
	 * <p>
	 * 该版本添加了可分别团体查询的参数 gid。全体 gid=00，SNH48 gid=10， BEJ48 gid=11，GNZ48 gid=12...
	 * </p>
	 * 
	 * @param gid 团体ID，建议参数00
	 * @return 成员列表的json对象
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 * @throws JSONException
	 */
	public static JSONObject jsonAllMemberV2(String gid)
			throws KeyManagementException, NoSuchAlgorithmException, IOException, JSONException {
		String result = httpsAllMemberV2(gid);
//		String startStr = "get_members_success(";
//		String endStr = ");";
//		if (result.startsWith(startStr) && result.endsWith(endStr)) {
//			result = result.substring(startStr.length());// 去头
//			result = result.substring(0, result.length() - endStr.length());// 去尾
//		}
		return JsonProcess.getJSONObjectByString(result);
	}

	/**
	 * 发送Https请求，获取SNH48成员的个人详细信息。
	 * 
	 * @param memberId 成员ID
	 * @return 成员个人信息的json对象
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 * @throws JSONException
	 */
	public static JSONObject jsonMember(long memberId)
			throws KeyManagementException, NoSuchAlgorithmException, IOException, JSONException {
		String jsonStr = httpsMember(memberId);
		JSONObject memberObj = JsonProcess.getJSONObjectByString(jsonStr);
		if (memberObj.getBoolean("success")) {
			return memberObj;
		}
		throw new HttpsPocketAuthenticateException(
				"HttpsURL.MEMBER：" + memberObj.getString("message") + "。参数：{memberId = " + memberId + "}");
	}

	/**
	 * 发送Https请求，获取SNH48成员的个人房间信息。
	 * 
	 * @param sourceId 成员ID，对应memberId
	 * @param type     请求类型(默认填0，暂不推荐其他参数。)
	 * @return 成员个人房间信息的json对象
	 * @throws JSONException
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */
	public static JSONObject jsonMemberRoom(long sourceId, int type)
			throws KeyManagementException, NoSuchAlgorithmException, IOException, JSONException {
		String jsonStr = httpsMemberRoom(sourceId, type);
		JSONObject roomObj = JsonProcess.getJSONObjectByString(jsonStr);
		if (roomObj.getBoolean("success") || roomObj.getString("message").equals("房间不存在")) {
			return roomObj;
		}
		refreshToken();
		throw new HttpsPocketAuthenticateException();
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
	 * @throws JSONException
	 */
	public static JSONObject jsonToken(String username, String password)
			throws KeyManagementException, NoSuchAlgorithmException, IOException, JSONException {
		String jsonStr = httpsToken(username, password);
		JSONObject loginObj = JsonProcess.getJSONObjectByString(jsonStr);
		if (loginObj.getBoolean("success")) {
			return loginObj;
		}
		throw new HttpsPocketAuthenticateException("HttpsURL.MEMBER：" + loginObj.getString("message")
				+ "。参数：{username = " + username + ", password = " + password + "}");
	}

	/**
	 * 发送Https请求，获取口袋48成员房间的消息列表。
	 * 
	 * @param memberId 成员ID
	 * @param roomId   房间ID
	 * @param nextTime 下条消息的时间戳。要获取最新消息该参数为0
	 * @return 房间消息的json对象
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 * @throws JSONException
	 */
	public static JSONObject jsonRoomMessage(String memberId, String roomId, long nextTime)
			throws KeyManagementException, NoSuchAlgorithmException, IOException, JSONException {
		String jsonStr = httpsRoomMessage(memberId, roomId, nextTime);
		JSONObject messageObj = JsonProcess.getJSONObjectByString(jsonStr);
		if (messageObj.getBoolean("success")) {
			return messageObj;
		}
		refreshToken();
		throw new HttpsPocketAuthenticateException(
				"HttpsURL.ROOM_MESSAGE：" + messageObj.getString("message") + "。参数：{memberId = " + memberId
						+ ", roomId = " + roomId + ", nextTime = " + String.valueOf(nextTime) + "}");
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
	 * @return 房间留言板消息的json对象。
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 * @throws JSONException
	 */
	public static JSONObject jsonRoomMessageALL(long nextTime, boolean needTop1Msg, long roomId)
			throws KeyManagementException, NoSuchAlgorithmException, IOException, JSONException {
		String jsonStr = httpsRoomMessageALL(nextTime, needTop1Msg, roomId);
		JSONObject messageObj = JsonProcess.getJSONObjectByString(jsonStr);
		if (messageObj.getBoolean("success")) {
			return messageObj;
		}
		refreshToken();
		throw new HttpsPocketAuthenticateException("HttpsURL.ROOM_MESSAGE_ALL：" + messageObj.getString("message")
				+ "。参数：{nextTime = " + String.valueOf(nextTime) + ", needTop1Msg = " + String.valueOf(needTop1Msg)
				+ ", roomId = " + String.valueOf(roomId) + "}");
	}

	/**
	 * 发送Https请求，获取口袋房间翻牌详情信息。
	 * <p>
	 * 本请求返回值包括问题、回答、提问人昵称、回答人昵称（重要）等。
	 * 
	 * @param questionId 问题ID
	 * @param answerId   回答ID
	 * @return 翻牌信息的json对象
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 * @throws JSONException
	 */
	public static JSONObject jsonFlipcardContent(String questionId, String answerId)
			throws KeyManagementException, NoSuchAlgorithmException, IOException, JSONException {
		String jsonStr = httpsFlipcardContent(questionId, answerId);
		JSONObject flipcardObj = JsonProcess.getJSONObjectByString(jsonStr);
		if (flipcardObj.getBoolean("success")) {
			return flipcardObj.getJSONObject("content");
		}
		throw new HttpsPocketAuthenticateException("HttpsURL.ROOM_MESSAGE_FLIPCARD：" + flipcardObj.getString("message")
				+ "。参数：{questionId = " + questionId + ", answerId = " + answerId + "}");
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
	 * @return 返回行程列表的JSON对象。
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 * @throws JSONException
	 */
	public static JSONObject jsonTrip(long lastTime, int groupId, boolean isMore)
			throws KeyManagementException, NoSuchAlgorithmException, IOException, JSONException {
		String jsonStr = httpsTrip(lastTime, groupId, isMore);
		JSONObject tripObj = JsonProcess.getJSONObjectByString(jsonStr);
		if (tripObj.getBoolean("success")) {
			return tripObj.getJSONObject("content");
		}
		throw new HttpsPocketAuthenticateException("HttpsURL.GROUP_TRIP：" + tripObj.getString("message")
				+ "。参数：{lastTime = " + lastTime + ", groupId = " + groupId + ", isMore = " + isMore + "}");
	}

	/**
	 * 发送HTTPS请求，获取口袋48的用户（部分）信息。
	 * 
	 * @param needMuteInfo
	 * @param userId       用户ID
	 * @return 返回用户（部分）信息的json对象。
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 * @throws JSONException
	 */
	public static JSONObject jsonPocketUser(int needMuteInfo, long userId)
			throws KeyManagementException, NoSuchAlgorithmException, IOException, JSONException {
		String jsonStr = httpsPocketUser(needMuteInfo, userId);
		JSONObject userObj = JsonProcess.getJSONObjectByString(jsonStr);
		if (userObj.getBoolean("success")) {
			return userObj.getJSONObject("content");
		}
		throw new HttpsPocketAuthenticateException("HttpsURL.USER_SMALL：" + userObj.getString("message")
				+ "。参数：{needMuteInfo = " + needMuteInfo + ", userId = " + userId + "}");
	}

	/**
	 * 发送Https请求，获取微博用户信息。
	 * 
	 * @param containerUserId 容器ID(用户)
	 * @return 用户信息的json对象
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 * @throws JSONException
	 */
	public static JSONObject jsonWeiboUser(Long containerUserId)
			throws KeyManagementException, NoSuchAlgorithmException, IOException, JSONException {
		String jsonStr = httpsWeiboUser(containerUserId);
		JSONObject userObj = JsonProcess.getJSONObjectByString(jsonStr);
		if (userObj.getInt("ok") == 1) {// 成功返回结果
			return userObj;
		}
		return null;
	}

	/**
	 * 发送Https请求，获取微博用户的动态列表。
	 * 
	 * @param containerDynamicId 动态数据的关键字段
	 * @return 动态的json对象
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 * @throws JSONException
	 */
	public static JSONObject jsonWeiboDynamic(Long containerDynamicId)
			throws KeyManagementException, NoSuchAlgorithmException, IOException, JSONException {
		String jsonStr = httpsWeiboDynamic(containerDynamicId);
		JSONObject dynamicObj = JsonProcess.getJSONObjectByString(jsonStr);
		if (dynamicObj.getInt("ok") == 1) {// 成功返回结果
			return dynamicObj;
		}
		return null;
	}

	/**
	 * 解析SNH48成员信息的json对象，将信息设置到{@link Member}中。
	 * 
	 * @param member       成员对象
	 * @param memberObject 成员信息json对象
	 * @throws JSONException
	 * @throws ParseException
	 */
	protected static void setMember(Member member, JSONObject memberObject) throws JSONException, ParseException {
		JSONObject content = memberObject.getJSONObject("content");
		JSONObject starInfo = content.getJSONObject("starInfo");
		JSONArray history = content.getJSONArray("history");

		member.setAvatar(getSourceUrl(starInfo.getString("starAvatar")));// 成员头像地址
		member.setName(starInfo.getString("starName"));// 名字
		member.setPinyin(starInfo.getString("pinyin"));// 名字拼音
		member.setAbbr(starInfo.getString("abbr"));// 名字缩写
		member.setBirthday(starInfo.getString("birthday"));// 生日
		member.setBirthplace(starInfo.getString("birthplace"));// 出生地
		member.setBloodType(starInfo.getString("bloodType"));// 血型
		member.setConstellation(starInfo.getString("constellation"));// 星座
		member.setHeight(starInfo.getInt("height"));// 身高
		member.setTeamId(starInfo.getLong("starTeamId"));// 所属队伍ID
		member.setTeamName(starInfo.getString("starTeamName"));// 所属队伍名字
		member.setGroupId(starInfo.getLong("starGroupId"));// 所属团体ID
		member.setGroupName(starInfo.getString("starGroupName"));// 所属团体名字
		member.setHistory(history.toString());// 成员历史
		member.setHobbies(starInfo.getString("hobbies"));// 爱好
		member.setJoinTime(DateUtil.getDateFormat(starInfo.getString("joinTime"), "yyyy-MM-dd"));// 加入时间
		member.setSpecialty(starInfo.getString("specialty"));// 特长
		member.setNickname(starInfo.getString("nickname"));// 昵称
	}

	/**
	 * 解析SNH48成员口袋48房间信息的json对象，将信息设置到{@link Member}中。
	 * 
	 * @param member        成员对象
	 * @param memberRoomObj 口袋房间信息json对象
	 * @throws JSONException
	 */
	protected static void setMemberRoom(Member member, JSONObject memberRoomObj) throws JSONException {
		if (memberRoomObj.getBoolean("success")) {// 房间存在
			JSONObject roomInfo = memberRoomObj.getJSONObject("content").getJSONObject("roomInfo");
			member.setRoomId(roomInfo.getLong("roomId"));// 房间ID
			member.setRoomName(roomInfo.getString("roomName"));// 房间名
			member.setTopic(roomInfo.getString("roomTopic"));// 房间话题
		} else {
			member.setRoomMonitor(memberRoomObj.getInt("status"));
		}
	}

	/**
	 * 解析SNH48成员口袋48房间消息的json对象，将信息设置到{@link RoomMessage}中。
	 * 
	 * @param roomMessage 房间消息对象
	 * @param indexObj    消息实例json对象
	 * @throws JSONException
	 * @throws ParseException
	 */
	protected static void setRoomMessage(RoomMessage roomMessage, JSONObject indexObj)
			throws JSONException, ParseException {
		JSONObject extInfoObj = new JSONObject(indexObj.getString("extInfo"));// 消息内容的主体对象
		JSONObject userObj = extInfoObj.getJSONObject("user");// 发送的用户对象

		roomMessage.setId(indexObj.getString("msgidClient"));// 消息ID
		roomMessage.setIsSend(1);// 是否发送，默认1(未发送)
		roomMessage.setMessageObject(extInfoObj.getString("messageType"));// 消息类型
		roomMessage.setMsgContent(getMsgContent(indexObj));// 消息内容
		roomMessage.setRoomId(extInfoObj.getLong("roomId"));// 房间ID
		roomMessage.setSenderId(userObj.getLong("userId"));// 发送人ID
		roomMessage.setSenderName(userObj.getString("nickName"));// 发送人昵称
		roomMessage.setMsgTime(DateUtil.getDateFormat(indexObj.getLong("msgTime")));// 发送时间
	}

	/**
	 * 获取按固定格式处理后的消息
	 * 
	 * @param indexObj 消息主体json对象
	 * @return 处理后的消息字符串
	 * @throws JSONException
	 */
	private static String getMsgContent(JSONObject indexObj) throws JSONException {
		StringBuffer msgContent = new StringBuffer();
		JSONObject extInfoObject = new JSONObject(indexObj.getString("extInfo"));// 消息内容的主体对象
		String messageType = extInfoObject.getString("messageType");// 消息类型

		if (messageType.equals("TEXT")) {// 普通文本类型
			msgContent.append(indexObj.getString("bodys"));

		} else if (messageType.equals("REPLY")) {// 回复类型
			msgContent.append("[回复]<br>");
			msgContent.append(indexObj.getString("bodys"));
			msgContent.append("<br>_____________________________<br>");
			msgContent.append(extInfoObject.getString("replyName"));
			msgContent.append(" : ");
			msgContent.append(extInfoObject.getString("replyText"));

		} else if (messageType.equals("IMAGE")) {// 图片类型
			msgContent.append("<img>");
			JSONObject bodysObject = new JSONObject(indexObj.getString("bodys"));
			msgContent.append(bodysObject.getString("url"));

		} else if (messageType.equals("LIVEPUSH")) {// 生放送类型
			msgContent.append("[生放送]<br>");
			msgContent.append(extInfoObject.getString("liveTitle"));
			msgContent.append("<br>");
			msgContent.append(HttpsURL.LIVE);
			msgContent.append(extInfoObject.getString("liveId"));
			msgContent.append("<img>");
			msgContent.append(getSourceUrl(extInfoObject.getString("liveCover")));

		} else if (messageType.equals("FLIPCARD")) {// 翻牌类型
			JSONObject contentObject = null;
			String questionId = extInfoObject.getString("questionId");
			String answerId = extInfoObject.getString("answerId");
			try {
				contentObject = jsonFlipcardContent(questionId, answerId);// 获取翻牌详情，目的是拿到这个翻牌是谁发的
			} catch (KeyManagementException | NoSuchAlgorithmException | IOException e) {
				log.error("获取翻牌消息异常questionId={}, answerId={} : {}", questionId, answerId, e.getMessage());
			}
			msgContent.append("[");
			msgContent.append(indexObj.getString("bodys"));
			msgContent.append("]<br>");
			msgContent.append(extInfoObject.getString("answer"));
			msgContent.append("<br>______________________________<br>");
			msgContent
					.append(contentObject != null ? contentObject.getString("userName") : questionId + "|" + answerId);
			msgContent.append(" : ");
			msgContent.append(extInfoObject.getString("question"));

		} else if (messageType.equals("EXPRESS")) {// 特殊表情类型
			msgContent.append("发送了一个特殊表情[");
			msgContent.append(extInfoObject.getString("emotionName"));
			msgContent.append("]，请到口袋48App查看。");

		} else if (messageType.equals("VIDEO")) {// 视频类型
			JSONObject bodysObject = new JSONObject(indexObj.getString("bodys"));
			msgContent.append("[视频]<br>点击➡️");
			msgContent.append(bodysObject.getString("url"));

		} else if (messageType.equals("VOTE")) {// 投票类型
			msgContent.append("[投票]<br>");
			msgContent.append(extInfoObject.getString("text"));
			msgContent.append("<br>详情请到口袋48App查看。");

		} else if (messageType.equals("AUDIO")) {// 语音类型
			msgContent.append("<audio>️");
			JSONObject bodysObject = new JSONObject(indexObj.getString("bodys"));
			msgContent.append(bodysObject.getString("url"));

		} else if (messageType.equals("PASSWORD_REDPACKAGE")) {// 口令红包
			msgContent.append("口令红包：");
			msgContent.append(extInfoObject.getString("redPackageTitle"));
			msgContent.append("<img>");
			msgContent.append(getSourceUrl(extInfoObject.getString("redPackageCover")));

		} else {
			msgContent.append("type error.");
			log.info("本条消息为未知的新类型: {}", messageType);
			log.info(indexObj.toString());
		}

		return msgContent.toString();
	}

	/**
	 * 解析SNH48成员口袋48房间留言板消息的json对象，将信息设置到{@link RoomMessageAll}中。
	 * 
	 * @param roomMessage 房间留言板消息对象
	 * @param indexObj    留言板消息实例json对象
	 * @throws JSONException
	 * @throws ParseException
	 */
	protected static void setRoomMessageAll(RoomMessageAll roomMessageAll, JSONObject indexObj) throws JSONException {
		JSONObject extInfoObj = new JSONObject(indexObj.getString("extInfo"));// 消息内容的主体对象
		JSONObject userObj = extInfoObj.getJSONObject("user");// 发送的用户对象
		String messageType = extInfoObj.getString("messageType");// 消息类型

		roomMessageAll.setId(indexObj.getString("msgidClient"));// 消息ID
		roomMessageAll.setMessageType(messageType);// 消息类型
		roomMessageAll.setRoomId(extInfoObj.getLong("roomId"));// 房间ID
		roomMessageAll.setSenderUserId(userObj.getLong("userId"));// 发送人ID
		roomMessageAll.setMessageTime(DateUtil.getDateFormat(indexObj.getLong("msgTime")));// 发送时间
		roomMessageAll.setMessageContent(getMessageContent(indexObj));// 消息内容

		if (messageType.equals("REPLY")) {// 回复
			roomMessageAll.setReplyMessageId(extInfoObj.getString("replyMessageId"));// 回复的消息ID
			roomMessageAll.setReplyName(extInfoObj.getString("replyName"));// 被回复人昵称

		} else if (messageType.equals("FLIPCARD")) {// 翻牌
			String questionId = extInfoObj.getString("questionId");
			String answerId = extInfoObj.getString("answerId");
			try {
				JSONObject contentObject = jsonFlipcardContent(questionId, answerId);// 获取翻牌详情，目的是拿到这个翻牌是谁发的
				roomMessageAll.setReplyName(contentObject.getString("userName"));// 被翻牌人昵称
			} catch (KeyManagementException | NoSuchAlgorithmException | IOException e) {
				log.error("获取翻牌消息异常questionId={}, answerId={} : {}", questionId, answerId, e.getMessage());
			}
		}
	}

	/**
	 * 获取留言板的消息(只取消息内容，不构造消息格式)。
	 * 
	 * @param indexObj 消息主体json对象
	 * @return 处理后的消息字符串
	 * @throws JSONException
	 */
	private static String getMessageContent(JSONObject indexObj) throws JSONException {
		StringBuilder msgContent = new StringBuilder();
		JSONObject extInfoObject = new JSONObject(indexObj.getString("extInfo"));// 消息内容的主体对象
		String messageType = extInfoObject.getString("messageType");// 消息类型

		if (messageType.equals("TEXT")) {// 普通文本类型
			msgContent.append(indexObj.getString("bodys"));

		} else if (messageType.equals("REPLY")) {// 回复类型
			msgContent.append(indexObj.getString("bodys"));

		} else if (messageType.equals("IMAGE")) {// 图片类型
			JSONObject bodysObject = new JSONObject(indexObj.getString("bodys"));
			msgContent.append(bodysObject.getString("url"));

		} else if (messageType.equals("LIVEPUSH")) {// 生放送类型
			msgContent.append(HttpsURL.LIVE);
			msgContent.append(extInfoObject.getString("liveId"));

		} else if (messageType.equals("FLIPCARD")) {// 翻牌类型
			msgContent.append(extInfoObject.getString("answer"));

		} else if (messageType.equals("EXPRESS")) {// 特殊表情类型
			msgContent.append(extInfoObject.getString("emotionName"));

		} else if (messageType.equals("VIDEO")) {// 视频类型
			JSONObject bodysObject = new JSONObject(indexObj.getString("bodys"));
			msgContent.append(bodysObject.getString("url"));

		} else if (messageType.equals("VOTE")) {// 投票类型
			msgContent.append(extInfoObject.getString("text"));

		} else if (messageType.equals("AUDIO")) {// 语音类型
			JSONObject bodysObject = new JSONObject(indexObj.getString("bodys"));
			msgContent.append(bodysObject.getString("url"));

		} else if (messageType.equals("PRESENT_TEXT")) {// 礼物类型
			msgContent.append(extInfoObject.getJSONObject("giftInfo").getString("giftName"));

		} else if (messageType.equals("SPECICAL_REDPACKAGE")) {// 新春红包
			msgContent.append(extInfoObject.getString("redPackageTitle"));

		} else {
			msgContent.append("Unknown message type.");
			log.info("本条消息为未知的新类型: {}", messageType);
			log.info(indexObj.toString());
		}
		return msgContent.toString();
	}

	/**
	 * 解析SNH48 Group行程的json对象，将信息设置到{@link Trip}中。
	 * 
	 * @param trip     行程表
	 * @param indexObj 行程json对象
	 * @throws JSONException
	 */
	protected static void setTrip(Trip trip, JSONObject indexObj) throws JSONException {
		long tripId = indexObj.getLong("tripId");
		int type = indexObj.getInt("type");
		String title = indexObj.getString("title");
		String subTitle = indexObj.getString("subTitle");
		String content = indexObj.getString("content");
		String joinMemberName = indexObj.getString("joinMemberName");
		long timestamp = indexObj.getLong("timestamp");

		trip.setId(tripId);
		trip.setType(type);
		trip.setTitle(title);
		trip.setSubTitle(subTitle);
		trip.setContent(content);
		trip.setJoinMemberName(joinMemberName);
		trip.setShowTime(DateUtil.getDateFormat(timestamp));

		if (indexObj.has("ticketInfo")) {
			JSONObject ticketInfo = indexObj.getJSONObject("ticketInfo");
			String ticketContent = ticketInfo.getString("content");

			trip.setTicketContent(ticketContent);
		}

		if (indexObj.has("liveInfo")) {
			JSONObject liveInfo = indexObj.getJSONObject("liveInfo");
			String liveContent = liveInfo.getString("content");
			int liveType = liveInfo.getInt("type");

			trip.setLiveContent(liveContent);
			trip.setLiveType(liveType);
		}

		if (indexObj.has("locationInfo")) {
			JSONObject locationInfo = indexObj.getJSONObject("locationInfo");
			String locationDetails = locationInfo.getString("details");
			String locationKeyword = locationInfo.getString("keyword");

			trip.setLocationDetails(locationDetails);
			trip.setLocationKeyword(locationKeyword);
		}
	}

	/**
	 * 解析口袋48用户信息的json对象，将信息设置到{@link PocketUser}中。
	 * 
	 * @param pocketUser 口袋48用户表
	 * @param userObj    口袋48用户信息json对象
	 * @throws JSONException
	 */
	protected static void setPocketUser(PocketUser pocketUser, JSONObject userObj) throws JSONException {
		JSONObject userInfo = userObj.getJSONObject("userInfo");

		long id = userInfo.getLong("userId");
		String nickname = userInfo.getString("nickname");
		String avatar = userInfo.getString("avatar");
		int level = userInfo.getInt("level");
		boolean vip = userInfo.getBoolean("vip");
		int role = userInfo.getInt("userRole");

		pocketUser.setId(id);// 用户ID
		pocketUser.setNickname(nickname);// 昵称
		pocketUser.setAvatar(getSourceUrl(avatar));// 头像地址
		pocketUser.setLevel(level);// 用户等级
		pocketUser.setVip(vip);// 是否是vip用户
		pocketUser.setRole(role);// 用户类型
	}

	/**
	 * 解析微博用户信息的json对象，将信息设置到{@link WeiboUser}中。
	 * 
	 * @param weiboUser 微博用户对象
	 * @param userObj   用户实例json对象
	 * @throws JSONException
	 */
	protected static void setWeiboUser(WeiboUser weiboUser, JSONObject userObj) throws JSONException {
		JSONObject data = userObj.getJSONObject("data");
		JSONObject userInfo = data.getJSONObject("userInfo");
		JSONObject tabsInfo = data.getJSONObject("tabsInfo");

		weiboUser.setAvatarHd(userInfo.getString("avatar_hd"));
		weiboUser.setContainerDynamicId(tabsInfo.getJSONArray("tabs").getJSONObject(1).getLong("containerid"));
		weiboUser.setFollowCount(userInfo.getInt("follow_count"));
		weiboUser.setFollowersCount(userInfo.getInt("followers_count"));
		weiboUser.setId(userInfo.getLong("id"));
		weiboUser.setUserName(userInfo.getString("screen_name"));
	}

	/**
	 * 解析微博用户动态的json对象，将信息设置到{@link Dynamic}中。
	 * 
	 * @param dynamic 微博动态对象
	 * @param mblog   动态主体的json对象
	 * @throws JSONException
	 */
	protected static void setWeiboDynamic(Dynamic dynamic, JSONObject mblog) throws JSONException {
		dynamic.setCreatedAt(mblog.getString("created_at"));// 创建时间
		dynamic.setId(mblog.getLong("id"));// 动态ID
		dynamic.setWeiboContent(getWeiboContent(mblog));// 微博内容
		if (mblog.has("title")) {// 是否为置顶博
			dynamic.setIsTop(true);
		} else {
			dynamic.setIsTop(false);
		}

		JSONObject userObject = mblog.getJSONObject("user");
		dynamic.setSenderName(userObject.getString("screen_name"));// 发送者姓名
		dynamic.setUserId(userObject.getLong("id"));// 发送者ID
		dynamic.setIsSend(1);// 是否已执行发送任务，默认为1否
		dynamic.setSyncDate(new Date());// 同步时间
	}

	/**
	 * 获取按固定格式处理后的微博动态
	 * 
	 * @param mblog 动态主体
	 * @return 处理后的动态字符串
	 * @throws JSONException
	 */
	private static String getWeiboContent(JSONObject mblog) throws JSONException {
		StringBuffer sb = new StringBuffer();
		String text = null;
		// 获取文本
		if (mblog.has("raw_text")) {// 当有特殊行文本时直接取，而不去解析普通html文本
			sb.append(mblog.getString("raw_text"));
		} else {
			text = mblog.getString("text").replace("<br />", "$br$");
			Document doc = Jsoup.parse(text);
			sb.append(doc.text().replace("$br$", "<br>"));
		}
		// 获取转发内容
		if (mblog.has("retweeted_status")) {
			sb.append("<br>--------------------");
			sb.append("<br>转发 @");
			JSONObject retStat = mblog.getJSONObject("retweeted_status");
			String retStatUserName = retStat.getJSONObject("user").getString("screen_name");
			sb.append(retStatUserName);
			sb.append("：<br>");
			sb.append(getWeiboContent(retStat));
		}
		// 获取图片
		if (mblog.has("pics")) {
			JSONArray pics = mblog.getJSONArray("pics");
			for (int i = 0; i < pics.length(); i++) {
				JSONObject pic = pics.getJSONObject(i);
				if (i == 0) {
					sb.append("<br>");
				}
				sb.append("<img>");
				sb.append(pic.getString("url"));
			}
		}
		return sb.toString();
	}

}
