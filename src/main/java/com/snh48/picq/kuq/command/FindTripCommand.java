package com.snh48.picq.kuq.command;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.snh48.picq.core.Common;
import com.snh48.picq.entity.snh48.Trip;
import com.snh48.picq.kuq.KuqManage;
import com.snh48.picq.repository.snh48.TripRepository;
import com.snh48.picq.utils.DateUtil;

import cc.moecraft.icq.command.CommandProperties;
import cc.moecraft.icq.command.interfaces.EverywhereCommand;
import cc.moecraft.icq.event.events.message.EventMessage;
import cc.moecraft.icq.user.User;

/**
 * 查看48行程
 * 
 * @author shiro
 *
 */
@Component
public class FindTripCommand implements EverywhereCommand {

	@Autowired
	private TripRepository tripRepository;

	@Override
	public CommandProperties properties() {
		return new CommandProperties(Common.COMMAND_NAME_FIND_TRIP, Common.COMMAND_ALIAS_FIND_TRIP);
	}

	@Override
	public String run(EventMessage event, User sender, String command, ArrayList<String> args) {
		List<Trip> tripList = null;
		Date showTime = DateUtil.getMidnight();
		String locationKeywordRegex = "北京|上海|广州";
		String typeRegex = "公演|冷餐";

		if (args == null || args.size() == 0) {
			tripList = tripRepository.findByTypeAndShowTimeAfterOrderByShowTimeAsc(1, showTime);

		} else if (args.size() == 1) {
			String arg = args.get(0);

			if (Pattern.matches(locationKeywordRegex, arg)) {
				tripList = tripRepository.findByTypeAndLocationKeywordAndShowTimeAfterOrderByShowTimeAsc(1, arg,
						showTime);

			} else if (Pattern.matches(typeRegex, arg)) {
				int type = "公演".equals(arg) ? 1 : 3;
				tripList = tripRepository.findByTypeAndShowTimeAfterOrderByShowTimeAsc(type, showTime);
			}

		} else {
			return "参数错误！";
		}

		if (tripList == null || tripList.size() < 1) {
			return "未查询到行程！";
		}

		respond(event, tripList);

		return null;
	}

	/**
	 * 回复成员资料
	 * 
	 * @param event 活动对象
	 * @param list  行程列表
	 */
	private void respond(EventMessage event, List<Trip> list) {
		String message = KuqManage.tripMessageBuilder(list);
		event.respond(message);
	}

}
