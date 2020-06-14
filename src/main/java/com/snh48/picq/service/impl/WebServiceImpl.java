package com.snh48.picq.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snh48.picq.core.Common.ExpireTime;
import com.snh48.picq.core.Common.ModianStatus;
import com.snh48.picq.core.Common.MonitorType;
import com.snh48.picq.core.Common.RedisKey;
import com.snh48.picq.dao.WebDao;
import com.snh48.picq.entity.modian.MoDianPoolProject;
import com.snh48.picq.exception.RepositoryException;
import com.snh48.picq.repository.modian.MoDianCommentRepository;
import com.snh48.picq.repository.modian.MoDianPoolProjectRepository;
import com.snh48.picq.repository.snh48.MemberRepository;
import com.snh48.picq.repository.snh48.RoomMessageRepository;
import com.snh48.picq.repository.weibo.DynamicRepository;
import com.snh48.picq.repository.weibo.WeiboUserRepository;
import com.snh48.picq.service.WebService;
import com.snh48.picq.utils.DateUtil;
import com.snh48.picq.utils.RedisUtil;
import com.snh48.picq.utils.StringUtil;
import com.snh48.picq.vo.MtboxVO;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@Transactional
public class WebServiceImpl implements WebService {

	/**
	 * 成员表DAO组件
	 */
	@Autowired
	private MemberRepository memberRepository;

	/**
	 * 微博用户表DAO组件
	 */
	@Autowired
	private WeiboUserRepository weiboUserRepository;

	/**
	 * 摩点集资项目表DAO组件
	 */
	@Autowired
	private MoDianPoolProjectRepository moDianPoolProjectRepository;

	/**
	 * 微博动态表DAO组件
	 */
	@Autowired
	private DynamicRepository dynamicRepository;

	/**
	 * 口袋房间消息表DAO组件
	 */
	@Autowired
	private RoomMessageRepository roomMessageRepository;

	/**
	 * 摩点评论表DAO组件
	 */
	@Autowired
	private MoDianCommentRepository moDianCommentRepository;

	/**
	 * 页面跳转请求DAO组件
	 */
	@Autowired
	private WebDao webDao;

	public Map<String, MtboxVO> getMtboxData() {
		Map<String, MtboxVO> map = new HashMap<String, MtboxVO>();
		MtboxVO mtbox1 = new MtboxVO();
		MtboxVO mtbox2 = new MtboxVO();
		MtboxVO mtbox3 = new MtboxVO();
		MtboxVO mtbox4 = new MtboxVO();
		MtboxVO mtbox5 = new MtboxVO();

		/* 第一组 */
		if (RedisUtil.exists(RedisKey.HTML_INDEX_MTBOX_1)) {
			mtbox1 = (MtboxVO) RedisUtil.get(RedisKey.HTML_INDEX_MTBOX_1);

		} else {
			int roomCount = 0;
			try {
				roomCount = memberRepository.countByNotRoomMonitor(MonitorType.NOTHING);
			} catch (Exception e) {
				log.error("MemberRepository.countByNotRoomMonitor失败，roomMonitor={}，异常：{}", MonitorType.NOTHING,
						e.toString());
				throw new RepositoryException("获取已开通口袋房间的成员个数失败", e);
			}

			mtbox1.setName("已开通口袋房间的成员");
			mtbox1.setData(roomCount);
			mtbox1.setIcon("<i class=\"fa fa-user-o\"></i>");

			String result = RedisUtil.setex(RedisKey.HTML_INDEX_MTBOX_1, mtbox1, ExpireTime.MINUTE_5);
			if ("error".equals(result)) {
				log.error(".mtbox中的数据存入Redis失败, mtbox={}", RedisKey.HTML_INDEX_MTBOX_1);
			}
		}

		/* 第二组 */
		if (RedisUtil.exists(RedisKey.HTML_INDEX_MTBOX_2)) {
			mtbox2 = (MtboxVO) RedisUtil.get(RedisKey.HTML_INDEX_MTBOX_2);

		} else {
			int monitorCount = 0;
			try {
				monitorCount = memberRepository.countByRoomMonitor(MonitorType.OPEN);
			} catch (Exception e) {
				log.error("MemberRepository.countByRoomMonitor失败，roomMonitor={}，异常：{}", MonitorType.NOTHING,
						e.toString());
				throw new RepositoryException("获取正在监控的口袋房间个数失败", e);
			}

			mtbox2.setName("正在监控的口袋房间");
			mtbox2.setData(monitorCount);
			mtbox2.setIcon("<i class=\"fa fa-star-o\"></i>");

			String result = RedisUtil.setex(RedisKey.HTML_INDEX_MTBOX_2, mtbox2, ExpireTime.MINUTE_5);
			if ("error".equals(result)) {
				log.error(".mtbox中的数据存入Redis失败, mtbox={}", RedisKey.HTML_INDEX_MTBOX_2);
			}
		}

		/* 第三组 */
		if (RedisUtil.exists(RedisKey.HTML_INDEX_MTBOX_3)) {
			mtbox3 = (MtboxVO) RedisUtil.get(RedisKey.HTML_INDEX_MTBOX_3);

		} else {
			int weiboCount = 0;
			try {
				weiboCount = (int) weiboUserRepository.count();
			} catch (Exception e) {
				log.error("WeiboUserRepository.count失败，异常：{}", e.toString());
				throw new RepositoryException("获取正在监控的微博个数失败", e);
			}

			mtbox3.setName("正在监控的微博");
			mtbox3.setData(weiboCount);
			mtbox3.setIcon("<i class=\"fa fa-weibo\"></i>");

			String result = RedisUtil.setex(RedisKey.HTML_INDEX_MTBOX_3, mtbox3, ExpireTime.MINUTE_5);
			if ("error".equals(result)) {
				log.error(".mtbox中的数据存入Redis失败, mtbox={}", RedisKey.HTML_INDEX_MTBOX_3);
			}
		}

		/* 第四组 */
		if (RedisUtil.exists(RedisKey.HTML_INDEX_MTBOX_4)) {
			mtbox4 = (MtboxVO) RedisUtil.get(RedisKey.HTML_INDEX_MTBOX_4);

		} else {
			int modianCount = 0;
			try {
				modianCount = moDianPoolProjectRepository.countByStatus(ModianStatus.COLLECTING);
			} catch (Exception e) {
				log.error("MoDianPoolProjectRepository.countByStatus失败，status={}，异常：{}", ModianStatus.COLLECTING,
						e.toString());
				throw new RepositoryException("获取正在监控的摩点项目个数失败", e);
			}

			mtbox4.setName("正在监控的摩点项目");
			mtbox4.setData(modianCount);
			mtbox4.setIcon("<i class=\"fa fa-credit-card\"></i>");

			String result = RedisUtil.setex(RedisKey.HTML_INDEX_MTBOX_4, mtbox4, ExpireTime.MINUTE_5);
			if ("error".equals(result)) {
				log.error(".mtbox中的数据存入Redis失败, mtbox={}", RedisKey.HTML_INDEX_MTBOX_4);
			}
		}

		/* 第五组 */
		if (RedisUtil.exists(RedisKey.HTML_INDEX_MTBOX_5)) {
			mtbox5 = (MtboxVO) RedisUtil.get(RedisKey.HTML_INDEX_MTBOX_5);

		} else {
			int dataCount = 0;
			try {
				dataCount = dynamicRepository.countGreaterDate(DateUtil.getMidnight())
						+ roomMessageRepository.countGreaterDate(DateUtil.getMidnight())
						+ moDianCommentRepository.countGreaterDate(DateUtil.getMidnight());
			} catch (Exception e) {
				log.error(
						"DynamicRepository.countGreaterDate or RoomMessageRepository.countGreaterDate or MoDianCommentRepository.countGreaterDate失败，异常：{}",
						e.toString());
				throw new RepositoryException("获取今日同步数据量失败", e);
			}

			mtbox5.setName("今日同步数据量"); // 微博+口袋房间+摩点
			mtbox5.setData(dataCount);
			mtbox5.setIcon("<i class=\"fa fa-bar-chart\"></i>");

			String result = RedisUtil.setex(RedisKey.HTML_INDEX_MTBOX_5, mtbox5, ExpireTime.MINUTE_5);
			if ("error".equals(result)) {
				log.error(".mtbox中的数据存入Redis失败, mtbox={}", RedisKey.HTML_INDEX_MTBOX_5);
			}
		}

		map.put("mtbox1", mtbox1);
		map.put("mtbox2", mtbox2);
		map.put("mtbox3", mtbox3);
		map.put("mtbox4", mtbox4);
		map.put("mtbox5", mtbox5);

		return map;
	}

	@SuppressWarnings("unchecked")
	public Map<String, MtboxVO> getDsData() {
		Map<String, MtboxVO> map = new HashMap<String, MtboxVO>();
		Map<String, Date> params = new HashMap<String, Date>();
		List<MtboxVO> activeRooms;
		List<MtboxVO> activeMembers;
		Date beginDate = null;
		Date endDate = null;
		/* 近7日活跃的口袋房间TOP5 */
		if (RedisUtil.exists(RedisKey.HTML_INDEX_DS_ACTIVE_ROOMS)) {
			activeRooms = (List<MtboxVO>) RedisUtil.get(RedisKey.HTML_INDEX_DS_ACTIVE_ROOMS);

		} else {
			try {
				beginDate = DateUtil.getMidnight(DateUtil.getDateFormat(DateUtil.countDayToStr(-7)));
				endDate = DateUtil.getNearMidnight(DateUtil.getDateFormat(DateUtil.countDayToStr(-1)));
			} catch (Exception e) {
				log.error("getDsData()中获取开始时间和结束时间失败，异常：{}", e.toString());
				throw new RuntimeException("getDsData()中获取开始时间和结束时间失败", e);
			}

			params.put("beginDate", beginDate);
			params.put("endDate", endDate);
			activeRooms = webDao.findActiveRoom(params);
		}

		int activeRoomsSize = activeRooms.size();
		for (int i = 0; i < 5; i++) {
			if (activeRoomsSize > i) {
				map.put("ds" + (i + 1), activeRooms.get(i));
			} else {
				MtboxVO mtbox = new MtboxVO();
				mtbox.setName("-");
				mtbox.setIcon("-");
				mtbox.setData("-");
				map.put("ds" + (i + 1), mtbox);
			}
		}

		/* 今日活跃的成员TOP5 */
		if (RedisUtil.exists(RedisKey.HTML_INDEX_DS_ACTIVE_MEMBERS)) {
			activeMembers = (List<MtboxVO>) RedisUtil.get(RedisKey.HTML_INDEX_DS_ACTIVE_MEMBERS);

		} else {
			beginDate = DateUtil.getMidnight(new Date());
			params.put("beginDate", beginDate);
			activeMembers = webDao.findActiveMembers(params);
		}

		int activeMembersSize = activeMembers.size();
		for (int i = 0; i < 5; i++) {
			if (activeMembersSize > i) {
				map.put("ds" + (i + 6), activeMembers.get(i));
			} else {
				MtboxVO mtbox = new MtboxVO();
				mtbox.setName("-");
				mtbox.setIcon("-");
				mtbox.setData("-");
				map.put("ds" + (i + 6), mtbox);
			}
		}

		String result1 = RedisUtil.setex(RedisKey.HTML_INDEX_DS_ACTIVE_ROOMS, activeRooms, ExpireTime.MINUTE_5);
		String result2 = RedisUtil.setex(RedisKey.HTML_INDEX_DS_ACTIVE_MEMBERS, activeMembers, ExpireTime.MINUTE_5);
		if ("error".equals(result1) || "error".equals(result2)) {
			log.error(".ds中的数据存入Redis失败, ds={} or {}", RedisKey.HTML_INDEX_DS_ACTIVE_ROOMS,
					RedisKey.HTML_INDEX_DS_ACTIVE_MEMBERS);
		}

		return map;
	}

	public String getProjectNames(String projectIds) {
		String[] ids = projectIds.split(",");
		String[] projectNames = new String[ids.length];
		double backerMoney = 0;
		for (int i = 0; i < ids.length; i++) {
			MoDianPoolProject project = moDianPoolProjectRepository.findById(Long.parseLong(ids[i])).get();
			if (project != null) {
				projectNames[i] = project.getName();
				backerMoney = backerMoney + project.getBackerMoney();
			}
		}
		StringBuffer result = new StringBuffer();
		result.append(StringUtil.join(projectNames, "；"));
		result.append("。");
		result.append("集资总额：￥");
		result.append(backerMoney);
		return result.toString();
	}

	public List<MtboxVO> getModianDetailsTable(String projectIds) {
		String[] ids = projectIds.split(",");
		List<MtboxVO> detailsTable = webDao.findModianDetailsTableByIds(ids);
		return detailsTable;
	}

}
