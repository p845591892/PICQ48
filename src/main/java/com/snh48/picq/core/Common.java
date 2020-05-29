package com.snh48.picq.core;

/**
 * 通用参数
 * 
 * @author shiro
 *
 */
public class Common {

	/*
	 * 失效时间
	 */
	public static final int EXPIRE_TIME_SECOND = 1;
	public static final int EXPIRE_TIME_SECOND_MINUTE = EXPIRE_TIME_SECOND * 60;
	public static final int EXPIRE_TIME_SECOND_HOUR = EXPIRE_TIME_SECOND_MINUTE * 60;
	public static final int EXPIRE_TIME_SECOND_DAY = EXPIRE_TIME_SECOND_HOUR * 24;
	public static final int EXPIRE_TIME_SECOND_WEEK = EXPIRE_TIME_SECOND_DAY * 7;
	public static final int EXPIRE_TIME_SECOND_MONTH = EXPIRE_TIME_SECOND_DAY * 30;
	public static final long EXPIRE_TIME_MILLISECOND_SECOND = 1000;
	public static final long EXPIRE_TIME_MILLISECOND_MINUTE = EXPIRE_TIME_MILLISECOND_SECOND * 60;

	/**
	 * 口袋48房间监控信息缓存的key（在后拼接roomId）
	 */
	public static final String ROOM_MONITOR = "room_monitor_";
	/**
	 * 监控QQ列表缓存的生存时间（10分钟）
	 */
	public static final int EXPIRE_TIME_SECOND_MONITOR = EXPIRE_TIME_SECOND_MINUTE * 10;
	/**
	 * 口袋48房间成员信息缓存的key（在后拼接roomId）
	 */
	public static final String ROOM_MEMBER = "room_member_";
	/**
	 * 成员信息缓存的生存时间（1小时）
	 */
	public static final int EXPIRE_TIME_SECOND_MEMBER = EXPIRE_TIME_SECOND_HOUR;
	/**
	 * 微博动态监控信息缓存的key（在后拼接userId）
	 */
	public static final String DYNAMIC_MONITOR = "dynamic_monitor_";

	/*
	 * 酷Q
	 */
	public static final String COMMAND_REPLAY_HELP = "请使用 -help/-h/-帮助 指令来获取详细操作。";
	public static final String[] ENABLE_COMMAND_MANAGER = new String[] { "!", "-" };

	public static class Command {
		public static final String HELP = "help";
		public static final String VERSION = "version";
		public static final String SET_ROOM_MONITOR = "setRoomMonitor";
		public static final String FIND_MEMBER = "findMember";
		public static final String FIND_TRIP = "findTrip";
		public static final String FRIEND_ADD = "friendAdd";
		public static final String GROUP_INVITE = "groupInvite";
	}

	public static class CommandAlias {
		public static final String[] HELP = new String[] { "h", "帮助" };
		public static final String[] VERSION = new String[] { "v", "版本" };
		public static final String[] SET_ROOM_MONITOR = new String[] { "setrm", "设置房间监控" };
		public static final String[] FIND_MEMBER = new String[] { "findm", "查找成员" };
		public static final String[] FIND_TRIP = new String[] { "findt", "查看行程" };
		public static final String[] FRIEND_ADD = new String[] { "fadd", "好友请求" };
		public static final String[] GROUP_INVITE = new String[] { "ginv", "群邀请" };
	}

	public class CommandCaption {
		public static final String HEAD = "指令名称(其他名称) | 参数 | 说明";
		public static final String HELP = "help(h/帮助) | 无 | 获取帮助，如：-help。";
		public static final String VERSION = "version(v/版本) | 无 | 获取版本。";
		public static final String SET_ROOM_MONITOR = "setRoomMonitor(setrm/设置房间监控) | [成员名字] [关键字] | 设置口袋房间监控。在群聊使用该指令则消息发向群，在好友聊天使用该指令则消息发向好友。多个关键字使用英文逗号分割，无关键字则全量发送，否则只有包含关键字的消息才会发送。";
		public static final String FIND_MEMBER = "findMember(findm/查找成员) | [名字]/[拼音缩写] | 根据名字或拼音缩写查找成员信息，如：-findm jjy";
		public static final String FIND_TRIP = "findTrip(findt/查看行程) | [北京/上海/广州/冷餐/公演/生日/节目] [冷餐/公演/生日/节目] | 根据北上广/行程类型查看行程，无参数默认查看全团所有行程，如：-findt 广州 公演。";
		public static final String FRIEND_ADD = "friendAdd(fadd/好友请求) | [flag码] [YES/NO] | 处理好友请求。flag码由上报好友请求时发送，同意填yes/y，不同意填no/n。（该功能仅限管理员）";
		public static final String GROUP_INVITE = "groupInvite(ginv/群邀请) | [flag码] [YES/NO] | 处理群邀请。flag码由上报邀请请求时发送，同意填yes/y，不同意填no/n。（该功能仅限管理员）";
	}

	public class MsgType {
		/**
		 * 文本
		 */
		public static final String TEXT = "TEXT";

		/**
		 * 回复
		 */
		public static final String REPLY = "REPLY";

		/**
		 * 图片
		 */
		public static final String IMAGE = "IMAGE";

		/**
		 * gif
		 */
		public static final String GIF = "gif";

		/**
		 * 生放送（直播/电台）
		 */
		public static final String LIVEPUSH = "LIVEPUSH";

		/**
		 * 翻牌
		 */
		public static final String FLIPCARD = "FLIPCARD";

		/**
		 * 特殊表情
		 */
		public static final String EXPRESS = "EXPRESS";

		/**
		 * 视频
		 */
		public static final String VIDEO = "VIDEO";

		/**
		 * 投票
		 */
		public static final String VOTE = "VOTE";

		/**
		 * 语音
		 */
		public static final String AUDIO = "AUDIO";

		/**
		 * 口令红包
		 */
		public static final String PASSWORD_REDPACKAGE = "PASSWORD_REDPACKAGE";

		/**
		 * 分享POST请求
		 */
		public static final String SHARE_POSTS = "SHARE_POSTS";

		/**
		 * 分享网站
		 */
		public static final String SHARE_WEB = "SHARE_WEB";

		/**
		 * 礼物
		 */
		public static final String PRESENT_TEXT = "PRESENT_TEXT";

		/**
		 * 新春红包
		 */
		public static final String SPECICAL_REDPACKAGE = "SPECICAL_REDPACKAGE";
	}
	
	/**
	 * 监控状态
	 * @author shiro
	 *
	 */
	public class MonitorType {
		public static final int  OPEN = 1;
		public static final int CLOS = 2;
		public static final int NOTHING = 404;
	}

}
