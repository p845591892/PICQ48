package com.snh48.picq.https;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import com.snh48.picq.entity.snh48.Member;
import com.snh48.picq.entity.snh48.RoomMessage;

import lombok.extern.log4j.Log4j2;

/**
 * 接口{@link Pocket48}的实现类。口袋48工具，封装了各种获取数据的方法。
 * <p>
 * 其中部分功能需要使用到redis缓存。
 * 
 * @author shiro
 *
 */
@Log4j2
public class Pocket48Tool extends JsonPICQ48 {

	/**
	 * 获取SNH48 Group成员的数据集合。通过Https的方式发送请求，获得json结果，解析后生成{@link Member}的集合。
	 * 
	 * @return {@link Member}集合
	 */
	@SuppressWarnings("unchecked")
	public static List<Member> getMemberList() {
		try {
			JSONObject allMemberObj = jsonAllMember();
			List<Member> memberList = new ArrayList<Member>();
			/* 遍历全体成员列表 */
			Iterator<String> iterator = allMemberObj.keys();
			while (iterator.hasNext()) {
				/* 获取成员个人资料 */
				String key = iterator.next();
				JSONObject memberObject = allMemberObj.getJSONObject(key);
				long memberId = memberObject.getLong("memberId");
				Member member = getMember(memberId);
				memberList.add(member);
			}
			return memberList;
		} catch (KeyManagementException | NoSuchAlgorithmException | IOException | JSONException e) {
			log.error("获取List<Member>异常：{}", e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取单个SNH48成员的数据。通过Https的方式发送请求，获得json结果，解析后生成{@link Member}对象。
	 * 
	 * @param memberId 成员ID
	 * @return {@link Member}
	 */
	public static Member getMember(long memberId) {
		try {
			JSONObject memberObj = jsonMember(memberId);
			Member member = new Member();
			member.setId(memberId);// 设置成员ID
			setMember(member, memberObj);
			JSONObject memberRoomObj = jsonMemberRoom(memberId, 0);
			setMemberRoom(member, memberRoomObj);
			return member;
		} catch (KeyManagementException | NoSuchAlgorithmException | IOException | JSONException | ParseException e) {
			log.error("获取Member异常：{}", e.getMessage());
		}
		return null;
	}

	/**
	 * 获取SNH48成员的口袋48房间消息。通过Https的方式发送请求，获得json结果，解析后生成{@link RoomMessage}的集合。
	 * 
	 * @param memberId 成员ID
	 * @param roomId   房间ID
	 * @param nextTime 下条消息的时间戳。该参数为0，则从最新的消息获取，否则从指定时间戳的消息之前获取。
	 * @return {@link RoomMessage}集合
	 */
	public static List<RoomMessage> getRoomMessageList(String memberId, String roomId, long nextTime) {
		try {
			JSONObject messageObj = jsonRoomMessage(memberId, roomId, nextTime);
			// 遍历消息json数组
			JSONArray messageArray = messageObj.getJSONObject("content").getJSONArray("message");
			List<RoomMessage> messageList = new ArrayList<RoomMessage>();
			for (int i = 0; i < messageArray.length(); i++) {
				JSONObject indexObj = messageArray.getJSONObject(i);
				RoomMessage roomMessage = new RoomMessage();
				setRoomMessage(roomMessage, indexObj);
				messageList.add(roomMessage);
			}
			return messageList;
		} catch (KeyManagementException | NoSuchAlgorithmException | IOException | JSONException | ParseException e) {
			log.error("获取List<RoomMessage>发生异常：{}", e.getMessage());
		}
		return null;
	}

}
