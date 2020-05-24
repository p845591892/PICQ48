package com.snh48.picq.kuq.command;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.snh48.picq.core.Common.Command;
import com.snh48.picq.core.Common.CommandAlias;
import com.snh48.picq.utils.SpringUtil;

import cc.moecraft.icq.PicqBotX;
import cc.moecraft.icq.command.CommandProperties;
import cc.moecraft.icq.command.interfaces.EverywhereCommand;
import cc.moecraft.icq.event.events.message.EventMessage;
import cc.moecraft.icq.sender.IcqHttpApi;
import cc.moecraft.icq.sender.message.MessageBuilder;
import cc.moecraft.icq.sender.returndata.returnpojo.get.RVersionInfo;
import cc.moecraft.icq.user.User;

/**
 * 查询版本指令，全收
 * 
 * @author shiro
 *
 */
@Component
public class VersionCommand extends AbstractCommand implements EverywhereCommand {

	@Override
	public CommandProperties properties() {
		return new CommandProperties(Command.VERSION, CommandAlias.VERSION);
	}

	@Override
	public String run(EventMessage event, User sender, String command, ArrayList<String> args) {
		IcqHttpApi icqHttpApi = SpringUtil.getBean(PicqBotX.class).getAccountManager().getNonAccountSpecifiedApi();
		RVersionInfo ver = icqHttpApi.getVersionInfo().getData();
		// 消息构造器
		MessageBuilder mb = new MessageBuilder();
		// Line 1
		mb.add("最后更新时间：2020-05-13").newLine();
		// Line 2
		mb.newLine().add("系统版本信息：");
		// Line 3
		mb.newLine().add("【PQIC48版本】  1.4.0");
		// Line 4
		mb.newLine().add("【PicqBotX版本】  4.12.0.1015.PRE").newLine();
		// Line 5
		mb.newLine().add("酷Q版本信息：");
		// Line 6
		mb.newLine().add("【型号】  " + ver.getCoolqEdition());
		// Line 7
		mb.newLine().add("【版本】  " + ver.getPluginVersion());
		return mb.toString();
	}

	@Override
	protected <T> void respond(EventMessage event, List<T> list) {

	}

	@Override
	protected <T> void respond(EventMessage event, T t) {

	}

}
