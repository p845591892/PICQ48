package com.snh48.picq.entity.snh48;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * @ClassName: Member
 * @Description: SNH48官方成员表
 * @author JuFF_白羽
 * @date 2018年7月10日 上午11:06:11
 */
@Data
@Entity
@Table(name = "T_SNH_MEMBER")
public class Member implements Serializable {

	private static final long serialVersionUID = -2784112823412247732L;

	/**
	 * 成员ID
	 */
	@Id
	@Column(name = "ID")
	private Long id;

	/**
	 * 成员头像地址
	 */
	@Column(name = "AVATAR", length = 500)
	private String avatar;

	/**
	 * 成员名字
	 */
	@Column(name = "NAME", length = 50)
	private String name;

	/**
	 * 成员名字拼音
	 */
	@Column(name = "PINYIN", length = 50)
	private String pinyin;

	/**
	 * 所属队伍ID
	 */
	@Column(name = "TEAM_ID")
	private Long teamId;

	/**
	 * 所属队伍名字
	 */
	@Column(name = "TEAM_NAME", length = 50)
	private String teamName;

	/**
	 * 所属团体ID
	 */
	@Column(name = "GROUP_ID")
	private Long groupId;

	/**
	 * 所属团体名字
	 */
	@Column(name = "GROUP_NAME", length = 50)
	private String groupName;

	/**
	 * 口袋房间ID
	 */
	@Column(name = "ROOM_ID")
	private Long roomId;

	/**
	 * 成员口袋房间监控状态：1开启，2关闭，404房间不存在
	 */
	@Column(name = "ROOM_MONITOR")
	private Integer roomMonitor;

	/**
	 * 口袋房间名字
	 */
	@Column(name = "ROOM_NAME", length = 200)
	private String roomName;

	/**
	 * 话题
	 */
	@Column(name = "TOPIC", length = 500)
	private String topic;

	/**
	 * 成员名字缩写
	 */
	@Column(name = "ABBR", length = 10)
	private String abbr;

	/**
	 * 加入日期
	 */
	@Column(name = "JOIN_TIME")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date joinTime;

	/**
	 * 生日
	 */
	@Column(name = "BIRTHDAY", length = 20)
	private String birthday;

	/**
	 * 出生地
	 */
	@Column(name = "BIRTHPLACE", length = 20)
	private String birthplace;

	/**
	 * 血型
	 */
	@Column(name = "BLOOD_TYPE", length = 5)
	private String bloodType;

	/**
	 * 星座
	 */
	@Column(name = "CONSTELLATION", length = 10)
	private String constellation;

	/**
	 * 身高
	 */
	@Column(name = "HEIGHT")
	private Integer height;

	/**
	 * 爱好
	 */
	@Column(name = "HOBBIES", length = 100)
	private String hobbies;

	/**
	 * 特长
	 */
	@Column(name = "SPECIALTY", length = 100)
	private String specialty;

	/**
	 * 成员历史
	 */
	@Column(name = "HISTORY", columnDefinition = "text")
	private String history;

	/**
	 * 昵称
	 */
	@Column(name = "NICKNAME", length = 50)
	private String nickname;

	public void buildRoom(JSONObject roomObj) throws JSONException {
		if (roomObj.getBoolean("success")) {// 房间存在
			JSONObject roomInfo = roomObj.getJSONObject("content").getJSONObject("roomInfo");
			setRoomId(roomInfo.getLong("roomId"));// 房间ID
			setRoomName(roomInfo.getString("roomName"));// 房间名
			setTopic(roomInfo.getString("roomTopic"));// 房间话题
		} else {
			setRoomMonitor(roomObj.getInt("status"));
		}
		
	}

}
