package com.snh48.picq.activemq;

/**
 * ActiveMQ消息队列的消息目的地
 * 
 * @author shiro
 *
 */
public class ActivemqPhysical {

	/**
	 * 队列模式：发送口袋房间消息
	 */
	public static final String QUEUE_SEND_ROOM_MESSAGE = "myQueue.sendRoomMessage";

	/**
	 * 队列模式：发送微博动态
	 */
	public static final String QUEUE_SEND_WEIBO_DYNAMIC = "myQueue.sendWeiboDynamic";

}
