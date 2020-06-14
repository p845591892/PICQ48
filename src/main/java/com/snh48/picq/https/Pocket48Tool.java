package com.snh48.picq.https;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import com.snh48.picq.entity.snh48.Member;
import com.snh48.picq.entity.snh48.PocketUser;
import com.snh48.picq.entity.snh48.RoomMessage;
import com.snh48.picq.entity.snh48.RoomMessageAll;
import com.snh48.picq.entity.snh48.Trip;

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
	 * 获取SNH48 Group成员的数据集合。通过Https的方式发送请求，获得json结果，解析后生成{@link Member}的集合。（V1版接口）
	 * 
	 * @return {@link Member}集合
	 */
	@SuppressWarnings("unchecked")
	public static List<Member> getMemberList() {
		List<Member> memberList = new ArrayList<Member>();
		try {
			JSONObject allMemberObj = jsonAllMember();
			/* 遍历全体成员列表 */
			Iterator<String> iterator = allMemberObj.keys();
			while (iterator.hasNext()) {
				/* 获取成员个人资料 */
				String key = iterator.next();
				JSONObject memberObject = allMemberObj.getJSONObject(key);
				long memberId = memberObject.getLong("memberId");
				Member member = getMember(memberId);
				
				if (null != member) {
					memberList.add(member);
				}
			}
			return memberList;
		} catch (Exception e) {
			log.error("getMemberList() 获取List<Member>异常：{}", e.toString());
		}
		return null;
	}

	/**
	 * 获取SNH48 Group成员的数据集合。通过Https的方式发送请求，获得json结果，解析后生成{@link Member}的集合。（V2版接口）
	 * <p>
	 * 该版本添加了可分别团体查询的参数 gid。全体 gid=00，SNH48 gid=10， BEJ48 gid=11，GNZ48 gid=12...
	 * </p>
	 * 
	 * @param gid 团体ID，建议参数00
	 * @return {@link Member}集合
	 */
	public static List<Member> getMemberListV2(String gid) {
		try {
			JSONObject obj = jsonAllMemberV2(gid);
			List<Member> memberList = new ArrayList<Member>();
			JSONArray array = obj.getJSONArray("rows");
			for (int i = 0; i < array.length(); i++) {
				JSONObject indexObj = array.getJSONObject(i);
				long memberId = indexObj.getLong("pocket_id");
				Member member = null;
				if (memberId == 0) {
					System.out.println(indexObj.toString());
				} else {
					member = getMember(memberId);
					memberList.add(member);
					Thread.sleep(1000);
				}
			}
			return memberList;
		} catch (Exception e) {
			log.error("getMemberListV2() 获取List<Member>异常：{}", e.toString());
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
			Member member = new Member();
			JSONObject memberObj = jsonMember(memberId);
			member.setId(memberId);// 设置成员ID
			setMember(member, memberObj);
			return member;
		} catch (Exception e) {
			log.error("getMember(long memberId)异常：{}", e.toString());
		}
		return null;
	}
	
//	public static Member getMemberRoom(Member member) {
//		jsonMemberRoom()
//		return member;
//	}

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

			int messageArraySize = messageArray.length();
			if (messageArraySize < 1) {
				throw new NullPointerException("获取到的口袋房间消息列表为空");
			}

			for (int i = 0; i < messageArraySize; i++) {
				JSONObject indexObj = messageArray.getJSONObject(i);
				RoomMessage roomMessage = new RoomMessage();
				setRoomMessage(roomMessage, indexObj);
				messageList.add(roomMessage);
			}
			return messageList;
		} catch (Exception e) {
			log.error("获取口袋房间消息失败。memberId={}, roomId={}, nextTime={}, 异常：{}", 
					memberId, roomId, nextTime, e.toString());
		}
		return null;
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
	 * @return {@link RoomMessageAll}集合。
	 */
	public static List<RoomMessageAll> getRoomMessageAllList(long nextTime, boolean needTop1Msg, long roomId) {
		try {
			JSONObject messageObj = jsonRoomMessageALL(nextTime, needTop1Msg, roomId);
			// 遍历消息json数组
			JSONArray messageArray = messageObj.getJSONObject("content").getJSONArray("message");
			List<RoomMessageAll> messageList = new ArrayList<RoomMessageAll>();
			for (int i = 0; i < messageArray.length(); i++) {
				JSONObject indexObj = messageArray.getJSONObject(i);
				RoomMessageAll roomMessageAll = new RoomMessageAll();
				setRoomMessageAll(roomMessageAll, indexObj);
				messageList.add(roomMessageAll);
			}
			return messageList;
		} catch (Exception e) {
			log.error("获取List<RoomMessageAll>发生异常：{}", e.toString());
		}
		return null;
	}

	/**
	 * 获取SNH48 Group的行程单。通过Https的方式发送请求，获得json结果，解析后生成{@link Trip}的集合。
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
	 * @return {@link Trip}集合
	 */
	public static List<Trip> getTripList(long lastTime, int groupId, boolean isMore) {
		try {
			JSONObject tripObj = jsonTrip(lastTime, groupId, isMore);
			// 遍历数组
			JSONArray tripArray = tripObj.getJSONArray("data");
			List<Trip> tripList = new ArrayList<Trip>();
			for (int i = 0; i < tripArray.length(); i++) {
				JSONObject indexObj = tripArray.getJSONObject(i);
				Trip trip = new Trip();
				setTrip(trip, indexObj);
				tripList.add(trip);
			}
			return tripList;
		} catch (Exception e) {
			log.error("获取List<Trip>发生异常：{}", e.toString());
		}
		return null;
	}

	/**
	 * 获取口袋48用户信息。通过Https的方式发送请求，获得json结果，解析后生成{@link PocketUser}的对象。
	 * 
	 * @param needMuteInfo
	 * @param userId       用户
	 * @return 返回{@link PocketUser}
	 */
	public static PocketUser getPocketUser(int needMuteInfo, long userId) {
		try {
			JSONObject userObj = jsonPocketUser(needMuteInfo, userId);
			PocketUser pocketUser = new PocketUser();
			setPocketUser(pocketUser, userObj);
			return pocketUser;
		} catch (Exception e) {
			log.error("获取PocketUser发生异常：{}", e.toString());
		}
		return null;
	}

	@Override
	public <T> List<T> get(Class<T> clazz) {
		// TODO Auto-generated method stub
		return null;
	}

}
