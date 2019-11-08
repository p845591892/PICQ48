package com.snh48.picq.kuq.command;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import com.snh48.picq.core.Common;

import cc.moecraft.icq.command.CommandProperties;
import cc.moecraft.icq.command.interfaces.EverywhereCommand;
import cc.moecraft.icq.event.events.message.EventMessage;
import cc.moecraft.icq.sender.message.MessageBuilder;
import cc.moecraft.icq.user.User;

/**
 * 查询版本指令，全收
 * 
 * @author shiro
 *
 */
@Component
public class VersionCommand implements EverywhereCommand {

	@Override
	public CommandProperties properties() {
		return new CommandProperties(Common.COMMAND_NAME_VERSION, Common.COMMAND_ALIAS_VERSION);
	}

	@Override
	public String run(EventMessage event, User sender, String command, ArrayList<String> args) {
		// 消息构造器
		MessageBuilder mb = new MessageBuilder();
		// Line 1
		mb.add("最后更新：2019-11-07");
		// Line 2
		mb.newLine().add("【PQIC48】  v1.2.0");
		// Line 3
		mb.newLine().add("【PicqBotX】  v4.12.0.991.PRE");
		return mb.toString();
	}

}
