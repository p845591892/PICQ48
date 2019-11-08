package com.snh48.picq.kuq.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.snh48.picq.config.KuqProperties;
import com.snh48.picq.core.Common;

import cc.moecraft.icq.event.EventHandler;
import cc.moecraft.icq.event.IcqListener;
import cc.moecraft.icq.event.events.request.EventFriendRequest;
import cc.moecraft.icq.sender.IcqHttpApi;
import cc.moecraft.icq.sender.message.MessageBuilder;

/**
 * 请求事件监听器
 * 
 * @author shiro
 *
 */
@Component
public class EventRequestListener extends IcqListener {

	@Autowired
	private KuqProperties properties;

	/**
	 * 加好友请求事件
	 */
	@EventHandler
	public void onFRevent(EventFriendRequest event) {
		// 构造消息
		MessageBuilder mb = new MessageBuilder();
		mb.add(" 有一条好友请求").add("(" + event.getFlag() + ")");
		mb.newLine().add(event.getUserId() + "：").add(event.getComment());
		mb.newLine().add(Common.COMMAND_CAPTION_FRIEND_ADD);
		// 获取api
		IcqHttpApi icqHttpApi = event.getBot().getAccountManager().getNonAccountSpecifiedApi();
		// 发送消息给管理员
		icqHttpApi.sendPrivateMsg(properties.getAdminId(), mb.toString());
	}

}
