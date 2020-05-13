package com.snh48.picq.kuq.command;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.snh48.picq.core.Common;

import cc.moecraft.icq.command.CommandProperties;
import cc.moecraft.icq.command.interfaces.GroupCommand;
import cc.moecraft.icq.event.events.message.EventGroupMessage;
import cc.moecraft.icq.event.events.message.EventMessage;
import cc.moecraft.icq.user.Group;
import cc.moecraft.icq.user.GroupUser;

/**
 * 设置口袋房间监控指令（群）
 * 
 * @author shiro
 *
 */
@Component
public class SetRoomMonitorGroupCommand extends AbstractCommand implements GroupCommand {

	@Override
	public CommandProperties properties() {
		return new CommandProperties(Common.COMMAND_NAME_SET_ROOM_MONITOR, Common.COMMAND_ALIAS_SET_ROOM_MONITOR);
	}

	@Override
	public String groupMessage(EventGroupMessage event, GroupUser sender, Group group, String command,
			ArrayList<String> args) {

		return "抱歉，该功能还在开发当中。";
	}

	@Override
	protected <T> void respond(EventMessage event, List<T> list) {

	}

	@Override
	protected <T> void respond(EventMessage event, T t) {

	}

}
