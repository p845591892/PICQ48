package com.snh48.picq.kuq.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.snh48.picq.config.KuqProperties;
import com.snh48.picq.service.QQCommunityService;
import com.snh48.picq.utils.SpringUtil;

import cc.moecraft.icq.event.EventHandler;
import cc.moecraft.icq.event.IcqListener;
import cc.moecraft.icq.event.events.notice.EventNoticeFriendAdd;
import cc.moecraft.icq.event.events.notice.groupmember.decrease.EventNoticeGroupMemberKickBot;
import cc.moecraft.icq.event.events.notice.groupmember.increase.EventNoticeGroupMemberIncrease;
import cc.moecraft.icq.sender.IcqHttpApi;
import cc.moecraft.icq.sender.message.MessageBuilder;
import cc.moecraft.icq.sender.message.components.ComponentAt;
import cc.moecraft.icq.sender.message.components.ComponentShake;

/**
 * 提醒事件监听
 * 
 * @author shiro
 *
 */
@Component
public class EventNoticeListener extends IcqListener {

	@Autowired
	private KuqProperties properties;

	/**
	 * 加好友提醒
	 */
	@EventHandler
	public void onFAEvent(EventNoticeFriendAdd event) {
		System.out.println("加好友提醒");
		System.out.println(event.toString());
	}

	/**
	 * 自己被踢事件
	 */
	@EventHandler
	public void onENGMKBEvent(EventNoticeGroupMemberKickBot event) {
		long groupId = event.getGroupId();// 群号
		String groupName = event.getGroupMethods().getGroup().getInfo().getGroupName();// 群名称
		long operatorId = event.getOperatorId();// 操作人
		/* 构造消息 */
		MessageBuilder mb = new MessageBuilder();
		mb.add("被 " + operatorId + " 踢出群[" + groupName + "(" + groupId + ")]");
		IcqHttpApi icqHttpApi = event.getHttpApi();
		// 抖一抖，并发送消息给管理员
		icqHttpApi.sendPrivateMsg(properties.getAdminId(), new MessageBuilder().add(new ComponentShake()).toString(),
				false);
		icqHttpApi.sendPrivateMsg(properties.getAdminId(), mb.toString());
		/* 删除数据库中的数据 */
		SpringUtil.getBean(QQCommunityService.class).deleteQQCommunity(String.valueOf(groupId));
	}

	/**
	 * 群员增加事件
	 * 
	 * @throws InterruptedException
	 */
	@EventHandler
	public void onENGMIEvent(EventNoticeGroupMemberIncrease event) throws InterruptedException {
		long groupId = event.getGroupId();// 群号
		String groupName = event.getGroupMethods().getGroup().getInfo().getGroupName();// 群名称
		long userId = event.getUserId();// 新入群的QQ号
		/* 构造消息 */
		MessageBuilder mb = new MessageBuilder();
		mb.add(new ComponentAt(userId));// 艾特该QQ
		mb.add("欢迎加入【" + groupName + "】这个大家庭。");
		/* 3秒后发送 */
		Thread.sleep(3000);
		IcqHttpApi icqHttpApi = event.getHttpApi();
		icqHttpApi.sendGroupMsg(groupId, mb.toString(), false);
	}

}
