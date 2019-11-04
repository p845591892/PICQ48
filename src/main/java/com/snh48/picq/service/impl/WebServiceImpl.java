package com.snh48.picq.service.impl;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snh48.picq.dao.WebDao;
import com.snh48.picq.entity.modian.MoDianPoolProject;
import com.snh48.picq.repository.modian.MoDianCommentRepository;
import com.snh48.picq.repository.modian.MoDianPoolProjectRepository;
import com.snh48.picq.repository.snh48.MemberRepository;
import com.snh48.picq.repository.snh48.RoomMessageRepository;
import com.snh48.picq.repository.weibo.DynamicRepository;
import com.snh48.picq.repository.weibo.WeiboUserRepository;
import com.snh48.picq.service.WebService;
import com.snh48.picq.utils.DateUtil;
import com.snh48.picq.utils.StringUtil;
import com.snh48.picq.vo.MtboxVO;

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
		/* 第一组 */
		MtboxVO mtbox1 = new MtboxVO();
		mtbox1.setName("已开通口袋房间的成员");
		mtbox1.setData(memberRepository.countByNotRoomMonitor(404));
		mtbox1.setIcon("<i class=\"fa fa-user-o\"></i>");
		/* 第二组 */
		MtboxVO mtbox2 = new MtboxVO();
		mtbox2.setName("正在监控的口袋房间");
		mtbox2.setData(memberRepository.countByRoomMonitor(1));
		mtbox2.setIcon("<i class=\"fa fa-star-o\"></i>");
		/* 第三组 */
		MtboxVO mtbox3 = new MtboxVO();
		mtbox3.setName("正在监控的微博");
		mtbox3.setData(weiboUserRepository.count());
		mtbox3.setIcon("<i class=\"fa fa-weibo\"></i>");
		/* 第四组 */
		MtboxVO mtbox4 = new MtboxVO();
		mtbox4.setName("正在监控的摩点项目");
		mtbox4.setData(moDianPoolProjectRepository.countByStatus("众筹中"));
		mtbox4.setIcon("<i class=\"fa fa-credit-card\"></i>");
		/* 第五组 */
		MtboxVO mtbox5 = new MtboxVO();
		mtbox5.setName("今日同步数据量");// 微博+口袋房间+摩点
		mtbox5.setData(dynamicRepository.countGreaterDate(DateUtil.getMidnight())
				+ roomMessageRepository.countGreaterDate(DateUtil.getMidnight())
				+ moDianCommentRepository.countGreaterDate(DateUtil.getMidnight()));
		mtbox5.setIcon("<i class=\"fa fa-bar-chart\"></i>");

		map.put("mtbox1", mtbox1);
		map.put("mtbox2", mtbox2);
		map.put("mtbox3", mtbox3);
		map.put("mtbox4", mtbox4);
		map.put("mtbox5", mtbox5);
		return map;
	}

	public Map<String, MtboxVO> getDsData() throws ParseException {
		Map<String, MtboxVO> map = new HashMap<String, MtboxVO>();

		/* 近7日活跃的口袋房间TOP5 */
		Date beginDate = DateUtil.getMidnight(DateUtil.getDateFormat(DateUtil.countDay(-7)));
		Date endDate = DateUtil.getNearMidnight(DateUtil.getDateFormat(DateUtil.countDay(-1)));
		Map<String, Date> params = new HashMap<String, Date>();
		params.put("beginDate", beginDate);
		params.put("endDate", endDate);
		List<MtboxVO> activeRooms = webDao.findActiveRoom(params);
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
		beginDate = DateUtil.getMidnight(new Date());
		params.put("beginDate", beginDate);
		List<MtboxVO> activeMembers = webDao.findActiveMembers(params);
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
