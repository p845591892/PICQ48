package com.snh48.picq.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snh48.picq.core.Common.ExpireTime;
import com.snh48.picq.core.Common.RedisKey;
import com.snh48.picq.entity.taoba.TaobaDetail;
import com.snh48.picq.entity.taoba.TaobaJoin;
import com.snh48.picq.entity.taoba.TaobaMonitor;
import com.snh48.picq.exception.RepositoryException;
import com.snh48.picq.https.TaobaTool;
import com.snh48.picq.repository.taoba.TaobaDetailRepository;
import com.snh48.picq.repository.taoba.TaobaJoinRepository;
import com.snh48.picq.repository.taoba.TaobaMonitorRepository;
import com.snh48.picq.service.TaobaService;
import com.snh48.picq.utils.RedisUtil;
import com.snh48.picq.utils.StringUtil;
import com.snh48.picq.vo.TaobaMonitorVO;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@Transactional
public class TaobaServiceImpl implements TaobaService {

	@Autowired
	private TaobaDetailRepository taobaDetailRepository;

	@Autowired
	private TaobaJoinRepository taobaJoinRepository;

	@Autowired
	private TaobaMonitorRepository taobaMonitorRepository;

	@Override
	public List<TaobaDetail> getDetailsByRunningNot(boolean running) {
		List<TaobaDetail> details = new ArrayList<TaobaDetail>();
		try {
			details = taobaDetailRepository.findByRunningNot(running);
		} catch (Exception e) {
			log.error("DetailRepository.findByRunningNot失败，running={}，异常：{}", running, e.toString());
			throw new RepositoryException("DetailRepository.findByRunningNot失败", e);
		}
		return details;
	}

	@Override
	public void saveDetail(TaobaDetail detail) {
		try {
			taobaDetailRepository.save(detail);
		} catch (Exception e) {
			log.error("DetailRepository.saveDetail失败，异常：{}", e.toString());
			throw new RepositoryException("DetailRepository.saveDetail失败", e);
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
			throw new RepositoryException("TaobaJoinRepository.findById失败", e);
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
			throw new RepositoryException("TaobaJoinRepository.findFirstByDetailIdOrderByCreatTimeDesc失败", e);
		}
		return null;
	}

	@Override
	public void saveJoins(List<TaobaJoin> joins) {
		try {
			taobaJoinRepository.saveAll(joins);
		} catch (Exception e) {
			log.error("TaobaJoinRepository.saveAll失败，异常：{}", e.toString());
			throw new RepositoryException("TaobaJoinRepository.saveAll失败", e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TaobaMonitorVO> getCacheTaobaMonitor(long detailId) {
		String key = RedisKey.TAOBA_MONITOR_ + detailId;
		if (RedisUtil.exists(key)) {
			return (List<TaobaMonitorVO>) RedisUtil.get(key);
		}
		List<TaobaMonitorVO> vos = new ArrayList<TaobaMonitorVO>();
		try {
			vos = taobaMonitorRepository.findTaobaMonitorAndQQCommunityByDetailId(detailId);
		} catch (Exception e) {
			log.error("TaobaMonitorRepository.findTaobaMonitorAndQQCommunityByDetailId失败，detailId={}，异常：{}", detailId,
					e.toString());
			throw new RepositoryException("TaobaMonitorRepository.findTaobaMonitorAndQQCommunityByDetailId失败", e);
		}
		RedisUtil.setex(key, vos, ExpireTime.MINUTE_10);
		return vos;
	}

	@Override
	public List<TaobaDetail> getDetails() {
		List<TaobaDetail> details = new ArrayList<TaobaDetail>();
		try {
			details = taobaDetailRepository.findAll();
		} catch (Exception e) {
			log.error("TaobaDetailRepository.findAll失败，异常：{}", e.toString());
			throw new RepositoryException("TaobaDetailRepository.findAll失败", e);
		}
		return details;
	}

	@Override
	public void saveMonitor(TaobaMonitor monitor) {
		try {
			taobaMonitorRepository.save(monitor);
		} catch (Exception e) {
			log.error("TaobaMonitorRepository.save失败，异常：{}", e.toString());
			throw new RepositoryException("TaobaMonitorRepository.save失败", e);
		}
		refreshMonitorCache(monitor.getDetailId());
	}

	@Override
	public void deleteMonitor(long monitorId, long detailId) {
		try {
			taobaMonitorRepository.deleteById(monitorId);
		} catch (Exception e) {
			log.error("TaobaMonitorRepository.deleteById失败，id={}，异常：{}", monitorId, e.toString());
			throw new RepositoryException("TaobaMonitorRepository.deleteById失败", e);
		}
		refreshMonitorCache(detailId);
	}

	/**
	 * 刷新监控配置缓存
	 * 
	 * @param detailId 项目ID
	 */
	private void refreshMonitorCache(long detailId) {
		String key = RedisKey.TAOBA_MONITOR_ + detailId;
		try {
			List<TaobaMonitorVO> vos = taobaMonitorRepository.findTaobaMonitorAndQQCommunityByDetailId(detailId);
			RedisUtil.setex(key, vos, ExpireTime.MINUTE_10);
		} catch (Exception e) {
			log.error("设置桃叭监控配置缓存失败，detailId={}，异常：{}", detailId, e.toString());
		}
	}

	@Override
	public int add(String detailUrl) {
		String api = "https://www.tao-ba.club/#/pages/idols/detail";
		if (!detailUrl.contains(api)) {
			return 2;
		}
		String keyword = "\\?id\\=";
		String detailIdStr = detailUrl.split(keyword)[1].trim();
		if (!StringUtil.isNumeric(detailIdStr)) {
			return 2;
		}
		TaobaDetail detail = TaobaTool.getDetail(Long.parseLong(detailIdStr));
		long detailId = detail.getId();
		Map<String, Object> rankMap = TaobaTool.getRank(detailId);
		detail.setDetailUrl(detailUrl);
		detail.setJoinUser((Integer) rankMap.get("juser"));
		List<TaobaJoin> joins = new ArrayList<TaobaJoin>();

		int limit = 20;
		int offset = 0;
		boolean ismore = false;
		int size = 0;
		do {
			List<TaobaJoin> tempJoins = TaobaTool.getTaobaJoin(ismore, limit, detailId, offset);
			size = tempJoins.size();
			if (size > 0) {
				limit += 20;
				offset += 20;
				ismore = true;
			} else {
				ismore = false;
			}
			joins.addAll(tempJoins);
		} while (ismore);

		try {
			taobaDetailRepository.save(detail);
			taobaJoinRepository.saveAll(joins);
			return 1;
		} catch (Exception e) {
			log.error("TaobaDetailRepository.save or TaobaJoinRepository.saveAll失败，detailId={}，异常：{}", detailId,
					e.toString());
			throw new RepositoryException("TaobaDetailRepository.save or TaobaJoinRepository.saveAll失败", e);
		}
	}

	@Override
	public void delete(String detailIds) {
		String[] detailIdArry = detailIds.split(",");
		for (String detailId : detailIdArry) {
			long id = Long.parseLong(detailId);
			try {
				taobaMonitorRepository.deleteByDetailId(id);
				taobaJoinRepository.deleteByDetailId(id);
				taobaDetailRepository.deleteById(id);
			} catch (Exception e) {
				log.error(
						"TaobaMonitorRepository.deleteByDetailId or TaobaJoinRepository.deleteByDetailId or TaobaDetailRepository.deleteById失败，detailId={}，异常：{}",
						detailIds, e.toString());
				throw new RepositoryException(
						"TaobaMonitorRepository.deleteByDetailId or TaobaJoinRepository.deleteByDetailId or TaobaDetailRepository.deleteById失败",
						e);
			}
		}
	}

	@Override
	public TaobaDetail getDetail(long id) {
		RepositoryException exc = null;
		try {
			Optional<TaobaDetail> optional = taobaDetailRepository.findById(id);
			if (optional.isPresent()) {
				return optional.get();
			}
		} catch (Exception e) {
			log.error("TaobaDetailRepository.findById失败，id={}，异常：{}", id, e.toString());
			exc = new RepositoryException(
					"TaobaMonitorRepository.deleteByDetailId or TaobaJoinRepository.deleteByDetailId or TaobaDetailRepository.deleteById失败",
					e);
		}
		throw exc;
	}

}
