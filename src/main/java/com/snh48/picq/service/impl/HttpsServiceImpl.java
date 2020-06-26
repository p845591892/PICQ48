package com.snh48.picq.service.impl;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snh48.picq.core.Common.MonitorType;
import com.snh48.picq.core.Common.RedisKey;
import com.snh48.picq.core.Common.SleepMillis;
import com.snh48.picq.dao.MemberDao;
import com.snh48.picq.entity.snh48.Member;
import com.snh48.picq.entity.weibo.WeiboUser;
import com.snh48.picq.https.JsonPICQ48;
import com.snh48.picq.repository.snh48.MemberRepository;
import com.snh48.picq.service.HttpsService;
import com.snh48.picq.utils.Https;
import com.snh48.picq.utils.RedisUtil;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@Transactional
public class HttpsServiceImpl implements HttpsService {

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private MemberDao memberDao;

	@Override
	public WeiboUser getWeiboUser(Long containerUserId)
			throws KeyManagementException, NoSuchAlgorithmException, IOException, JSONException {
		Https https = new Https();
		Map<String, String> params = new HashMap<String, String>();
		params.put("containerid", String.valueOf(containerUserId));

		Map<String, String> requestPropertys = new HashMap<String, String>();
		requestPropertys.put("Accept", "application/json, text/plain, */*");
		requestPropertys.put("User-Agent",
				"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_4) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/12.1 Safari/605.1.15");
		requestPropertys.put("X-Requested-With", "XMLHttpRequest");

		String result = https.setDataType("GET").setUrl("https://m.weibo.cn/api/container/getIndex").setParams(params)
				.setRequestProperty(requestPropertys).send();
		JSONObject jsonObject = new JSONObject(result);

		if (jsonObject.getInt("ok") == 1) {// 成功返回结果
			JSONObject data = jsonObject.getJSONObject("data");
			JSONObject userInfo = data.getJSONObject("userInfo");
			JSONObject tabsInfo = data.getJSONObject("tabsInfo");

			WeiboUser weiboUser = new WeiboUser();
			weiboUser.setAvatarHd(userInfo.getString("avatar_hd"));
			weiboUser.setContainerUserId(containerUserId);
			weiboUser.setContainerDynamicId(tabsInfo.getJSONArray("tabs").getJSONObject(1).getLong("containerid"));
			weiboUser.setFollowCount(userInfo.getInt("follow_count"));
			weiboUser.setFollowersCount(userInfo.getInt("followers_count"));
			weiboUser.setId(userInfo.getLong("id"));
			weiboUser.setUserName(userInfo.getString("screen_name"));

			return weiboUser;

		}
		return null;
	}

	@Override
	public long getRoomId(Long memberId) {
		Long roomId = (Long) RedisUtil.get(RedisKey.CONVERSATION_ + memberId);
		if (null != roomId) {
			return roomId;
		}

		try {
			JSONObject json = JsonPICQ48.jsonConversation(0);
			JSONArray array = json.getJSONArray("conversations");

			for (int i = 0; i < array.length(); i++) {
				JSONObject conversation = array.getJSONObject(i);
				long ownerId = conversation.getLong("ownerId");
				long targetId = conversation.getLong("targetId");
				if (ownerId == memberId) {
					roomId = targetId;
				}

				RedisUtil.set(RedisKey.CONVERSATION_ + ownerId, targetId);
			}

			if (null == roomId) {
				roomId = 0l;
			}
		} catch (Exception e) {
			log.error("获取关注成员口袋房间ID错误，异常：{}", e.toString());
			roomId = -1l;
		}
		return roomId;
	}

	@Override
	public void syncMember() {
		log.info("[开始] 更新成员列表");
		List<Member> members = memberRepository.findAll();
		for (Member member : members) {
			log.info("当前更新成员：{}", member.getName());

			try {
				Thread.sleep(SleepMillis.REQUEST);
			} catch (InterruptedException e) {
				log.error("syncMember休眠线程失败，异常：{}", e.toString());
			}

			Long memberId = member.getId();
			Long roomId = member.getRoomId();
			if (null == roomId) { // 获取不到房间ID
				roomId = (Long) RedisUtil.get(RedisKey.CONVERSATION_ + memberId);
				if (null == roomId) {
					continue;
				}
			}

			int hisRoomMonitor = member.getRoomMonitor();
			try {
				JSONObject roomObj = JsonPICQ48.jsonMemberRoom(roomId, 0);
				member.buildRoom(roomObj);
			} catch (Exception e) {
				log.error("获取{}房间信息失败或者构建Member失败，异常：{}", member.getName(), e.toString());
				continue;
			}

			/* 1.判断房间是否被关闭 */
			if (member.getRoomMonitor() != MonitorType.NOTHING) {

				/* 2.房间是否关闭后开 */
				if (hisRoomMonitor != MonitorType.OPEN && hisRoomMonitor != MonitorType.CLOS) {
					member.setRoomMonitor(MonitorType.CLOS); // 强制为监控关闭

					/* 3.房间一直开着 */
				} else {
					member.setRoomMonitor(hisRoomMonitor); // 保持使用旧状态
				}
			}

			try {
				JSONObject memberObj = JsonPICQ48.jsonMember(memberId);
				JsonPICQ48.setMember(member, memberObj);
			} catch (Exception e) {
				log.error("获取{}成员信息失败或者构建Member失败，异常：{}", member.getName(), e.toString());
			}

			/* 修改/新增成员资料 */
			int row = memberDao.updateMemberById(member);
			if (row == 0) {// 说明为新成员，需要再进行insert
				memberRepository.save(member);
			}
		}
		log.info("[结束] 更新成员列表");
	}

}
