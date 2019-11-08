//package com.snh48.picq.kuq.listener;
//
//import org.springframework.stereotype.Component;
//
//import cc.moecraft.icq.event.EventHandler;
//import cc.moecraft.icq.event.IcqListener;
//import cc.moecraft.icq.event.events.message.EventPrivateMessage;
//import cc.moecraft.icq.sender.returndata.returnpojo.get.RStrangerInfo;
//
///**
// * 私聊监听器
// * 
// * @author shiro
// *
// */
//@Component
//public class EventPrivateMessageListener extends IcqListener {
//
//	@EventHandler
//	public void onEventPrivateMessage(EventPrivateMessage event) {
//		RStrangerInfo info = event.getSender().getInfo();
//		System.out.println(info.getNickname() + "(" + info.getUserId() + ")：" + event.getMessage());
//	}
//
//}
