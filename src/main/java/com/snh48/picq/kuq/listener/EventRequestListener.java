package com.snh48.picq.kuq.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.snh48.picq.config.KuqProperties;
import com.snh48.picq.core.Common.CommandCaption;

import cc.moecraft.icq.event.EventHandler;
import cc.moecraft.icq.event.IcqListener;
import cc.moecraft.icq.event.events.request.EventFriendRequest;
import cc.moecraft.icq.event.events.request.EventGroupInviteRequest;
import cc.moecraft.icq.sender.IcqHttpApi;
import cc.moecraft.icq.sender.message.MessageBuilder;
import cc.moecraft.icq.sender.message.components.ComponentShake;

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
	 * 所有请求事件
	 */
//	@EventHandler
//	public void onEREvent(EventRequest event) {
//		System.out.println("所有请求事件");
//		System.out.println(event.toString());
//	}

	/**
	 * 加好友请求事件
	 */
	@EventHandler
	public void onFRevent(EventFriendRequest event) {
		// 构造消息
		MessageBuilder mb = new MessageBuilder();
		mb.add("有一条好友请求").add("(flag = " + event.getFlag() + ")");
		mb.newLine().add(event.getUserId() + "：").add(event.getComment());
		mb.newLine().add(CommandCaption.FRIEND_ADD);
		// 获取api
		IcqHttpApi icqHttpApi = event.getHttpApi();
		// 抖一抖，并发送消息给管理员
		icqHttpApi.sendPrivateMsg(properties.getAdminId(), new MessageBuilder().add(new ComponentShake()).toString(),
				false);
		icqHttpApi.sendPrivateMsg(properties.getAdminId(), mb.toString());
	}

	/**
	 * 拉你入群请求事件
	 */
	@EventHandler
	public void onEGIREvent(EventGroupInviteRequest event) {
		// 构造消息
		MessageBuilder mb = new MessageBuilder();
		mb.add("有一条群邀请请求").add("(flag = " + event.getFlag() + ")");
		mb.newLine().add("【" + event.getSelfId() + "】").add("邀请你加入Q群：").add(event.getGroupId());
		mb.newLine().add(CommandCaption.GROUP_INVITE);
		// 获取api
		IcqHttpApi icqHttpApi = event.getHttpApi();
		// 抖一抖，并发送消息给管理员
		icqHttpApi.sendPrivateMsg(properties.getAdminId(), new MessageBuilder().add(new ComponentShake()).toString(),
				false);
		icqHttpApi.sendPrivateMsg(properties.getAdminId(), mb.toString());
	}

}
