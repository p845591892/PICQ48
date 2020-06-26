package com.snh48.picq.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snh48.picq.core.Common.ExpireTime;
import com.snh48.picq.core.Common.RedisKey;
import com.snh48.picq.entity.taoba.TaobaDetail;
import com.snh48.picq.entity.taoba.TaobaJoin;
import com.snh48.picq.repository.taoba.TaobaDetailRepository;
import com.snh48.picq.repository.taoba.TaobaJoinRepository;
import com.snh48.picq.repository.taoba.TaobaMonitorRepository;
import com.snh48.picq.service.TaobaService;
import com.snh48.picq.utils.RedisUtil;
import com.snh48.picq.vo.TaobaMonitorVO;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@Transactional
public class TaobaServiceImpl implements TaobaService {

	@Autowired
	private TaobaDetailRepository detailRepository;

	@Autowired
	private TaobaJoinRepository taobaJoinRepository;
	
	@Autowired
	private TaobaMonitorRepository taobaMonitorRepository;

	@Override
	public List<TaobaDetail> getDetailsByRunningNot(boolean running) {
		List<TaobaDetail> details = new ArrayList<TaobaDetail>();
		try {
			details = detailRepository.findByRunningNot(running);
		} catch (Exception e) {
			log.error("DetailRepository.findByRunningNot失败，running={}，异常：{}", running, e.toString());
		}
		return details;
	}

	@Override
	public void saveDetail(TaobaDetail detail) {
		try {
			detailRepository.save(detail);
		} catch (Exception e) {
			log.error("DetailRepository.saveDetail失败，异常：{}", e.toString());
		}
	}

	@Override
	public TaobaJoin getJoin(long id) {
		try {
			Optional<TaobaJoin> optional = taobaJoinRepository.findById(id);
			if (optional.isPresent()) {
				return optional.get();
			}
		} catch (Exception e) {
			log.error("TaobaJoinRepository.findById失败，id={}，异常：{}", id, e.toString());
		}
		return null;
	}

	@Override
	public Date getCurrentJoinCreatTime(long detailId) {
		try {
			TaobaJoin join = taobaJoinRepository.findFirstByDetailIdOrderByCreatTimeDesc(detailId);
			if (null != join) {
				return join.getCreatTime();
			}
		} catch (Exception e) {
			log.error("TaobaJoinRepository.findFirstByDetailIdOrderByCreatTimeDesc失败，detailId={}，异常：{}", detailId,
					e.toString());
		}
		return null;
	}

	@Override
	public void saveJoins(List<TaobaJoin> joins) {
		try {
			taobaJoinRepository.saveAll(joins);
		} catch (Exception e) {
			log.error("TaobaJoinRepository.saveAll失败，异常：{}", e.toString());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TaobaMonitorVO> getCacheTaobaMonitor(long detailId) {
		String key = RedisKey.TAOBA_MONITOR_ + detailId;
		if (RedisUtil.exists(key)) {
			return (List<TaobaMonitorVO>) RedisUtil.get(key);
		}
		List<TaobaMonitorVO> vos = taobaMonitorRepository.findTaobaMonitorAndQQCommunityByDetailId(detailId);
		RedisUtil.setex(key, vos, ExpireTime.MINUTE_10);
		return vos;
	}

}
