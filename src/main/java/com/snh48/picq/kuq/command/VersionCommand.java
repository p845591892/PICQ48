package com.snh48.picq.kuq.command;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.snh48.picq.core.Common;

import cc.moecraft.icq.PicqBotX;
import cc.moecraft.icq.command.CommandProperties;
import cc.moecraft.icq.command.interfaces.EverywhereCommand;
import cc.moecraft.icq.event.events.message.EventMessage;
import cc.moecraft.icq.sender.IcqHttpApi;
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

	@Autowired
	private PicqBotX bot;

	@Override
	public CommandProperties properties() {
		return new CommandProperties(Common.COMMAND_NAME_VERSION, Common.COMMAND_ALIAS_VERSION);
	}

	@Override
	public String run(EventMessage event, User sender, String command, ArrayList<String> args) {
		IcqHttpApi icqHttpApi = bot.getAccountManager().getNonAccountSpecifiedApi();
		// 消息构造器
		MessageBuilder mb = new MessageBuilder();
		// Line 1
		mb.add("最后更新：2019-11-14");
		// Line 2
		mb.newLine().add("系统版本信息：");
		// Line 3
		mb.newLine().add("【PQIC48】  v1.3.0");
		// Line 4
		mb.newLine().add("【PicqBotX】  v4.12.0.991.PRE").newLine();
		// Line 5
		mb.newLine().add("酷Q版本信息：");
		// Line 6
		mb.newLine().add(icqHttpApi.getVersionInfo().toString());
		return mb.toString();
	}

}
