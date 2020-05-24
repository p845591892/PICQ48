package com.snh48.picq.kuq.command;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.snh48.picq.config.KuqProperties;
import com.snh48.picq.core.Common.Command;
import com.snh48.picq.core.Common.CommandAlias;
import com.snh48.picq.core.Common.CommandCaption;
import com.snh48.picq.service.QQCommunityService;
import com.snh48.picq.utils.SpringUtil;

import cc.moecraft.icq.command.CommandProperties;
import cc.moecraft.icq.command.interfaces.PrivateCommand;
import cc.moecraft.icq.event.events.message.EventMessage;
import cc.moecraft.icq.event.events.message.EventPrivateMessage;
import cc.moecraft.icq.user.User;

/**
 * 处理好友请求指令
 * 
 * @author shiro
 *
 */
@Component
public class FriendAddAdminCommand extends AbstractCommand implements PrivateCommand {

	@Autowired
	private KuqProperties properties;

	@Override
	public CommandProperties properties() {
		return new CommandProperties(Command.FRIEND_ADD, CommandAlias.FRIEND_ADD);
	}

	@Override
	public String privateMessage(EventPrivateMessage event, User sender, String command, ArrayList<String> args) {
		if (properties.getAdminId() != sender.getId()) {
			return "指令执行失败！非管理员。";
		}

		if (args == null || args.size() < 2) {
			return "参数错误！\n" + CommandCaption.FRIEND_ADD;
		}

		String flag = args.get(0);
		boolean approve = false;
		String approveTemp = args.get(1).toLowerCase();

		if ("yes".equals(approveTemp) || "y".equals(approveTemp)) {
			approve = true;
		} else if ("no".equals(approveTemp) || "n".equals(approveTemp)) {
			approve = false;
		} else {
			return "参数错误\n" + CommandCaption.FRIEND_ADD;
		}

		event.getHttpApi().setFriendAndRequest(flag, approve);
		// 不能用注解注入，会导致依赖注入死循环。
		SpringUtil.getBean(QQCommunityService.class).syncQQCommunity();

		return "操作成功，已同步QQ列表。";
	}

	@Override
	protected <T> void respond(EventMessage event, List<T> list) {

	}

	@Override
	protected <T> void respond(EventMessage event, T t) {

	}

}
