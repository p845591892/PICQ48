package com.snh48.picq.service.impl;

import javax.net.ssl.HttpsURLConnection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snh48.picq.entity.QQCommunity;
import com.snh48.picq.repository.QQCommunityRepository;
import com.snh48.picq.repository.modian.CommentMonitorRepostiory;
import com.snh48.picq.repository.snh48.RoomMonitorRepository;
import com.snh48.picq.repository.weibo.DynamicMonitorRepository;
import com.snh48.picq.service.QQCommunityService;

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

	public int addQQCommunity(QQCommunity qqCommunity) {
		if (qqCommunity.getId() == null) {
			return 1;
		}
		if (qqCommunity.getCommunityName() == null || qqCommunity.getCommunityName().equals("")) {
			return 2;
		}
		QQCommunity qq = qqCommunityRepository.findById(qqCommunity.getId()).get();
		if (qq == null) {
			qq = qqCommunityRepository.save(qqCommunity);
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
			qqCommunityRepository.deleteById(Long.parseLong(idStr));
		}
		return HttpsURLConnection.HTTP_OK;
	}

}
