package com.snh48.picq.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.net.ssl.HttpsURLConnection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snh48.picq.core.QQType;
import com.snh48.picq.entity.QQCommunity;
import com.snh48.picq.repository.QQCommunityRepository;
import com.snh48.picq.repository.modian.CommentMonitorRepostiory;
import com.snh48.picq.repository.snh48.RoomMonitorRepository;
import com.snh48.picq.repository.taoba.TaobaMonitorRepository;
import com.snh48.picq.repository.weibo.DynamicMonitorRepository;
import com.snh48.picq.service.QQCommunityService;

import cc.moecraft.icq.PicqBotX;
import cc.moecraft.icq.sender.IcqHttpApi;
import cc.moecraft.icq.sender.returndata.returnpojo.get.RFriend;
import cc.moecraft.icq.sender.returndata.returnpojo.get.RGroup;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@Transactional
public class QQCommunityServiceImpl implements QQCommunityService {

	/**
	 * （yyh）QQ群信息表DAO组件
	 */
	@Autowired
	private QQCommunityRepository qqCommunityRepository;

	/**
	 * 摩点项目评论监控配置表DAO组件
	 */
	@Autowired
	private CommentMonitorRepostiory commentMonitorRepostiory;

	/**
	 * 微博动态监控配置表DAO组件
	 */
	@Autowired
	private DynamicMonitorRepository dynamicMonitorRepository;

	/**
	 * QQ群监控口袋房间表DAO组件
	 */
	@Autowired
	private RoomMonitorRepository roomMonitorRepository;

	@Autowired
	private TaobaMonitorRepository taobaMonitorRepository;

	@Autowired
	private PicqBotX bot;

	public int addQQCommunity(QQCommunity qqCommunity) {
		if (qqCommunity.getId() == null) {
			return 1;
		}
		if (qqCommunity.getCommunityName() == null || qqCommunity.getCommunityName().equals("")) {
			return 2;
		}
		Optional<QQCommunity> optional = qqCommunityRepository.findById(qqCommunity.getId());
		if (!optional.isPresent()) {
			qqCommunityRepository.save(qqCommunity);
			return HttpsURLConnection.HTTP_OK;
		} else {
			return 3;
		}
	}

	public int updateQQCommunity(QQCommunity qqCommunity) {
		if (qqCommunity.getId() == null) {
			return 1;
		}
		if (qqCommunity.getCommunityName() == null || qqCommunity.getCommunityName().equals("")) {
			return 2;
		}
		qqCommunityRepository.save(qqCommunity);
		return HttpsURLConnection.HTTP_OK;
	}

	public int deleteQQCommunity(String id) {
		String[] ids = id.split(",");
		for (String idStr : ids) {
			commentMonitorRepostiory.deleteByCommunityId(Long.parseLong(idStr));
			dynamicMonitorRepository.deleteByCommunityId(Long.parseLong(idStr));
			roomMonitorRepository.deleteByCommunityId(Long.parseLong(idStr));
			taobaMonitorRepository.deleteByCommunityId(Long.parseLong(idStr));
			qqCommunityRepository.deleteById(Long.parseLong(idStr));
		}
		return HttpsURLConnection.HTTP_OK;
	}

	@Override
	public boolean syncQQCommunity() {
		IcqHttpApi icqHttpApi = bot.getAccountManager().getNonAccountSpecifiedApi();
		if (icqHttpApi == null) {
			return false;
		}
		
		// 刷新缓存
		bot.getAccountManager().refreshCache();

		List<QQCommunity> newList = new ArrayList<QQCommunity>();
		// 好友列表
		List<RFriend> rFriendList = icqHttpApi.getFriendList().data;
		for (RFriend rFriend : rFriendList) {
			QQCommunity qq = new QQCommunity();
			qq.setId(rFriend.getUserId());
			qq.setCommunityName(rFriend.getNickname());
			qq.setQqType(QQType.FRIEND);
			newList.add(qq);
			log.info("同步好友[{}]", rFriend.getUserId());
		}

		// 群列表
		List<RGroup> rGroupList = icqHttpApi.getGroupList().data;
		for (RGroup rGroup : rGroupList) {
			QQCommunity qq = new QQCommunity();
			qq.setId(rGroup.getGroupId());
			qq.setCommunityName(rGroup.getGroupName());
			qq.setQqType(QQType.GROUP);
			newList.add(qq);
			log.info("同步群[{}]", rGroup.getGroupId());
		}
		qqCommunityRepository.saveAll(newList);
		
		// 差集
		List<QQCommunity> sourceList = qqCommunityRepository.findAll();
		sourceList.removeAll(newList);
		
		String[] ids = new String[sourceList.size()];
		for (int i = 0; i < sourceList.size(); i++) {
			ids[i] = String.valueOf(sourceList.get(i).getId());
		}
		deleteQQCommunity(String.join(",", ids));
		
		return true;
	}

	@Override
	public void truncate() {
		try {
			qqCommunityRepository.deleteAll();
		} catch (Exception e) {
			log.error("清空QQ列表失败，异常：{}", e.toString());
		}
	}

}
