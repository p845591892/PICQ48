package com.snh48.picq.quartz.job;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.transaction.annotation.Transactional;

import com.snh48.picq.core.Common.SleepMillis;
import com.snh48.picq.entity.QQCommunity;
import com.snh48.picq.entity.taoba.TaobaDetail;
import com.snh48.picq.entity.taoba.TaobaJoin;
import com.snh48.picq.https.TaobaTool;
import com.snh48.picq.kuq.KuqManage;
import com.snh48.picq.service.TaobaService;
import com.snh48.picq.utils.DateUtil;
import com.snh48.picq.vo.TaobaMonitorVO;

import cc.moecraft.icq.PicqBotX;
import cc.moecraft.icq.sender.IcqHttpApi;
import lombok.extern.log4j.Log4j2;

/**
 * 同步桃叭集资项目任务
 * 
 * @author shiro
 *
 */
@Log4j2
@Transactional
@DisallowConcurrentExecution // 任务串行注解
public class SyncTaobaJob extends QuartzJobBean {

	@Autowired
	private TaobaService taobaService;

	@Autowired
	private PicqBotX bot;

	@SuppressWarnings("unchecked")
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		log.info("--------------[开始] 同步桃叭集资项目任务。");

		List<TaobaDetail> details = taobaService.getDetailsByRunningNot(false);
		for (TaobaDetail detail : details) {
			try {
				Thread.sleep(SleepMillis.REQUEST);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			long id = detail.getId();
			TaobaDetail newDetail = TaobaTool.getDetail(id);

			if (null == newDetail) {
				continue;
			}

			boolean needUpdate = isNeedUpdate(detail, newDetail);
			if (!needUpdate) {
				continue;
			}

			Map<String, Object> rankMap = TaobaTool.getRank(id);
			newDetail.setJoinUser((int) rankMap.get("juser"));
			taobaService.saveDetail(newDetail);

			int limit = 20;
			int offset = 0;
			boolean ismore = false;
			List<TaobaJoin> joins = new ArrayList<TaobaJoin>();
			Date currentCreatTime = taobaService.getCurrentJoinCreatTime(id);

			do {
				List<TaobaJoin> tempJoins = TaobaTool.getTaobaJoin(ismore, limit, id, offset);
				int size = tempJoins.size();
				if (size > 0) {

					for (int i = 0; i < size; i++) {
						TaobaJoin tempJoin = tempJoins.get(i);

						if (null == currentCreatTime) {
							joins.add(tempJoin);
							if (i == size - 1) {
								offset += limit;
								ismore = true;
							} else {
								ismore = false;
							}

						} else {
							if (tempJoin.getCreatTime().getTime() > currentCreatTime.getTime()) {
								joins.add(tempJoin);
								if (i == size - 1) {
									offset += limit;
									ismore = true;
								} else {
									ismore = false;
								}
							} else {
								ismore = false;
								break;
							}
						}
					}

				} else {
					ismore = false;
				}

				try {
					Thread.sleep(SleepMillis.REQUEST);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} while (ismore);

			if (joins.size() > 0) {
				try {
					List<TaobaJoin> ranks = (List<TaobaJoin>) rankMap.get("ranks");
					sendMessage(joins, detail, ranks);
				} catch (InterruptedException e) {
					log.error("发送桃叭集资详细消息失败，项目：{}，异常：", detail.getTitle(), e.toString());
				}
				taobaService.saveJoins(joins);
			}
		}

		log.info("--------------[结束] 同步桃叭集资项目任务。");
	}

	/**
	 * 是否需要更新
	 */
	private boolean isNeedUpdate(TaobaDetail detail, TaobaDetail newDetail) {
		if (!newDetail.getRunning()) {
			return true;
		}
		if (null == detail.getSellstats()) {
			return true;
		}
		if (detail.getSellstats() < newDetail.getSellstats()) {
			return true;
		}
		return false;
	}

	/**
	 * 发送消息
	 */
	private void sendMessage(List<TaobaJoin> joins, TaobaDetail detail, List<TaobaJoin> ranks)
			throws InterruptedException {
		Collections.reverse(joins); // 逆序发送，从最前的消息开始发送
		List<TaobaMonitorVO> vos = taobaService.getCacheTaobaMonitor(detail.getId());
		IcqHttpApi icqHttpApi = bot.getAccountManager().getNonAccountSpecifiedApi();
		for (TaobaJoin join : joins) {
			String message = deserializeMsgContent(detail, join, ranks);
			System.out.println(message);
			for (TaobaMonitorVO vo : vos) {
				QQCommunity qqCommunity = vo.getQqCommunity();
				KuqManage.sendSyncMessage(icqHttpApi, message, qqCommunity);
			}

			join.setIsSend(true); // 已发送
		}
	}

	/**
	 * 构造消息
	 */
	private String deserializeMsgContent(TaobaDetail detail, TaobaJoin join, List<TaobaJoin> ranks) {
		StringBuilder sb = new StringBuilder();
		sb.append("来自桃叭：".concat(detail.getTitle()));
		sb.append("\n");
		sb.append(join.getNickname().concat(" ").concat(DateUtil.getDate(join.getCreatTime())));
		sb.append("\n");
		sb.append("支持了".concat(join.getMoney()).concat("元。"));
		sb.append("\n");
		sb.append("非常感谢支持🙏");
		sb.append("\n");
		sb.append("已筹集：".concat(detail.getDonation()).concat("元"));
		sb.append("\n");

		if (detail.getPercent() != 0) {
			sb.append("当前进度：".concat(String.valueOf(detail.getPercent())).concat("%"));
			sb.append("\n");
		}

		sb.append("集资链接：".concat(detail.getDetailUrl()));
		sb.append("\n");

		BigDecimal b1 = new BigDecimal(detail.getEndTime().getTime() - System.currentTimeMillis());
		BigDecimal b2 = new BigDecimal(1000 * 60 * 60);
		BigDecimal divide = b1.divide(b2, 2, RoundingMode.UP);
		sb.append("剩余时间：".concat(divide.toPlainString()).concat("小时"));

		if (null != ranks && ranks.size() > 0) {
			sb.append("\n");
			sb.append("______________________________");
			sb.append("\n");
			sb.append("当前集资TOP5：");
			int size = ranks.size() >= 5 ? 5 : ranks.size();
			for (int i = 0; i < size; i++) {
				TaobaJoin rank = ranks.get(i);
				sb.append("\n");
				sb.append((i + 1) + ".".concat(rank.getNickname()).concat(" ¥").concat(rank.getMoney()));
			}
		}
		return sb.toString();
	}

}