package com.snh48.picq.kuq.command;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.snh48.picq.core.Common.Command;
import com.snh48.picq.core.Common.CommandAlias;
import com.snh48.picq.core.Common.CommandCaption;

import cc.moecraft.icq.command.CommandProperties;
import cc.moecraft.icq.command.interfaces.EverywhereCommand;
import cc.moecraft.icq.event.events.message.EventMessage;
import cc.moecraft.icq.sender.message.MessageBuilder;
import cc.moecraft.icq.user.User;

/**
 * 查询帮助指令，全收
 * 
 * @author shiro
 *
 */
@Component
public class HelpCommand extends AbstractCommand implements EverywhereCommand {

	@Override
	public CommandProperties properties() {
		return new CommandProperties(Command.HELP, CommandAlias.HELP);
	}

	@Override
	public String run(EventMessage event, User sender, String command, ArrayList<String> args) {
		// 消息构造器
		MessageBuilder mb = new MessageBuilder();
		// Line 1
		mb.add("指令以 ! 或 - 开头，例：-help").newLine();
		// Line 2
		mb.newLine().add("附带参数使用空格分隔，例：-查找成员 zyg").newLine();
		// Line 3
		mb.newLine().add("指令列表：").newLine();
		// Line 4
		mb.newLine().add(CommandCaption.HEAD).newLine();
		// Line 5
		mb.newLine().add(CommandCaption.HELP).newLine();
		// Line 6
		mb.newLine().add(CommandCaption.VERSION).newLine();
		// Line 7
		mb.newLine().add(CommandCaption.SET_ROOM_MONITOR).newLine();
		// Line 8
		mb.newLine().add(CommandCaption.FIND_MEMBER).newLine();
		// Line 9
		mb.newLine().add(CommandCaption.FRIEND_ADD).newLine();
		// Line 10
		mb.newLine().add(CommandCaption.GROUP_INVITE).newLine();
		// Line 11
		mb.newLine().add(CommandCaption.FIND_TRIP);
		return mb.toString();
	}

	@Override
	protected <T> void respond(EventMessage event, List<T> list) {

	}

	@Override
	protected <T> void respond(EventMessage event, T t) {

	}

}
