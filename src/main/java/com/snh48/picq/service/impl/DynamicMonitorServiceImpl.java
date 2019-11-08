package com.snh48.picq.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snh48.picq.core.Common;
import com.snh48.picq.entity.weibo.DynamicMonitor;
import com.snh48.picq.entity.weibo.WeiboUser;
import com.snh48.picq.https.WeiboTool;
import com.snh48.picq.repository.weibo.DynamicMonitorRepository;
import com.snh48.picq.repository.weibo.WeiboUserRepository;
import com.snh48.picq.service.DynamicMonitorService;
import com.snh48.picq.utils.RedisUtil;
import com.snh48.picq.vo.DynamicMonitorVO;

@Service
@Transactional
public class DynamicMonitorServiceImpl implements DynamicMonitorService {

	@Autowired
	private WeiboUserRepository weiboUserRepository;

	@Autowired
	private DynamicMonitorRepository dynamicMonitorRepository;

	@Override
	public void addWeiboUser(Long containerId) {
		WeiboUser weiboUser = WeiboTool.getUser(containerId);
		weiboUserRepository.save(weiboUser);
	}

	@Override
	public void deleteWeiboUser(String userId) {
		dynamicMonitorRepository.deleteByUserId(Long.parseLong(userId));// 删除监控配置
		weiboUserRepository.deleteById(Long.parseLong(userId));// 删除微博用户
	}

	@Override
	public void addDynamicMonitor(DynamicMonitor dynamicMonitor) {
		dynamicMonitorRepository.save(dynamicMonitor);
	}

	@Override
	public void deleteDynamicMonitor(Long id) {
		dynamicMonitorRepository.deleteById(id);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<DynamicMonitorVO> getCache(long userId) {
		String keyStr = Common.DYNAMIC_MONITOR + String.valueOf(userId);
		if (RedisUtil.exists(keyStr)) {
			return (List<DynamicMonitorVO>) RedisUtil.get(keyStr);
		}
		List<DynamicMonitorVO> voList = dynamicMonitorRepository.findDynamicMonitorAndQQCommunityByUserId(userId);
		RedisUtil.setex(keyStr, voList, Common.EXPIRE_TIME_SECOND_MONITOR);
		return voList;
	}

}
