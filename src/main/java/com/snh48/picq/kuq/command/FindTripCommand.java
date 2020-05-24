package com.snh48.picq.kuq.command;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.snh48.picq.core.Common.Command;
import com.snh48.picq.core.Common.CommandAlias;
import com.snh48.picq.core.Common.CommandCaption;
import com.snh48.picq.entity.snh48.Trip;
import com.snh48.picq.kuq.KuqManage;
import com.snh48.picq.repository.snh48.TripRepository;
import com.snh48.picq.utils.DateUtil;

import cc.moecraft.icq.command.CommandProperties;
import cc.moecraft.icq.command.interfaces.EverywhereCommand;
import cc.moecraft.icq.event.events.message.EventMessage;
import cc.moecraft.icq.sender.message.MessageBuilder;
import cc.moecraft.icq.user.User;

/**
 * 查看48行程
 * 
 * @author shiro
 *
 */
@Component
public class FindTripCommand extends AbstractCommand implements EverywhereCommand {

	@Autowired
	private TripRepository tripRepository;

	@Override
	public CommandProperties properties() {
		return new CommandProperties(Command.FIND_TRIP, CommandAlias.FIND_TRIP);
	}

	@Override
	public String run(EventMessage event, User sender, String command, ArrayList<String> args) {
		List<Trip> tripList = new ArrayList<>();
		Date showTime = DateUtil.getMidnight();
		String locationKeywordRegex = "北京|上海|广州";
		String typeRegex = "公演|冷餐|生日";

		if (args == null || args.size() == 0) {
			tripList.addAll(tripRepository.findByShowTimeAfterOrderByShowTimeAsc(showTime));

		} else if (args.size() == 1) {
			String arg = args.get(0);
			if (Pattern.matches(locationKeywordRegex, arg)) {
				tripList.addAll(tripRepository.findByTypeAndLocationKeywordAndShowTimeAfterOrderByShowTimeAsc(1, arg,
						showTime));

			} else if (Pattern.matches(typeRegex, arg)) {
				int type;
				switch (arg) {
				case "公演":
					type = 1;
					break;
				case "冷餐":
					type = 3;
					break;
				case "生日":
					type = 0;
					break;
				default:
					type = -1;
					break;
				}
				tripList.addAll(tripRepository.findByTypeAndShowTimeAfterOrderByShowTimeAsc(type, showTime));

			} else {
				return "参数错误！\n" + CommandCaption.FIND_TRIP;
			}

		} else if (args.size() == 2) {
			String type = args.get(0);
			String locationKeyword = args.get(1);
			if (Pattern.matches(typeRegex, type) && Pattern.matches(locationKeywordRegex, locationKeyword)) {
				tripList.addAll(tripRepository.findByTypeAndLocationKeywordAndShowTimeAfterOrderByShowTimeAsc(
						Integer.parseInt(type), locationKeyword, showTime));

			} else {
				return "参数错误！\n" + CommandCaption.FIND_TRIP;
			}

		} else {
			return "参数错误！\n" + CommandCaption.FIND_TRIP;
		}

		respond(event, tripList);
		return null;
	}

	@Override
	protected <T> void respond(EventMessage event, List<T> list) {
		int size = list.size();
		if (size < 1) {
			event.respond("未查询到行程！");
		}

		MessageBuilder mb = new MessageBuilder();
		for (int i = 0; i < size; i++) {
			Trip trip = (Trip) list.get(i);
			mb.add(KuqManage.tripMessageBuilder(trip));

			if (i < size - 1) {
				mb.newLine().add("_____________________________").newLine();
			}
		}
		respond(event, mb);
	}

	@Override
	protected <T> void respond(EventMessage event, T t) {
		event.respond(t.toString());
	}

}
