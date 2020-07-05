package com.snh48.picq.core;

/**
 * 通用参数
 * 
 * @author shiro
 *
 */
public class Common {

	public class PicqSystem {
		public static final String UPDATE_TIME = "更新时间：2020-07-06";
		public static final String PICQ48_VERSION = "【PQIC48版本】  2.0.5";
		public static final String PICQ_BOT_X_VERSION = "【PicqBotX版本】  4.12.0.1019.PRE";
		public static final String UPDATE_MESSAGE = "本次更新：\n优化 一些功能的代码\n修正 一些页面显示数据\n新增 QQ列表-自定义欢迎消息功能";
	}

	public class ExpireTime {
		public static final int SECOND = 1;
		public static final int MINUTE = SECOND * 60;
		public static final int MINUTE_5 = MINUTE * 5;
		public static final int MINUTE_10 = MINUTE * 10;
		public static final int HOUR = MINUTE * 60;
		public static final int DAY = HOUR * 24;
		public static final int WEEK = DAY * 7;
		public static final int MONTH = DAY * 30;
	}

	public class SleepMillis {
		public static final long SECOND = 1000;
		public static final long SECOND_5 = SECOND * 5;
		public static final long SECOND_10 = SECOND * 10;
		public static final long MINUTE = SECOND * 60;
		public static final long REQUEST = SECOND_10;
	}

	public class RedisKey {
		/**
		 * 口袋48房间监控信息缓存的key（在后拼接roomId）
		 */
		public static final String ROOM_MONITOR = "room_monitor_";

		/**
		 * 口袋48房间成员信息缓存的key（在后拼接roomId）
		 */
		public static final String ROOM_MEMBER = "room_member_";

		/**
		 * 微博动态监控信息缓存的key（在后拼接userId）
		 */
		public static final String DYNAMIC_MONITOR = "dynamic_monitor_";

		/**
		 * 更新成员信息用关注的成员口袋房间ID缓存（在后拼接memberId）
		 */
		public static final String CONVERSATION_ = "conversation_";

		/**
		 * 口袋48登录
		 */
		public static final String TOKEN = "pocket_token";
		public static final String USER_ID = "pocket_user_id";

		/**
		 * 主页横向监控数据
		 */
		public static final String HTML_INDEX_MTBOX = "html_index_mtbox_";
		public static final String HTML_INDEX_MTBOX_1 = HTML_INDEX_MTBOX + "1";
		public static final String HTML_INDEX_MTBOX_2 = HTML_INDEX_MTBOX + "2";
		public static final String HTML_INDEX_MTBOX_3 = HTML_INDEX_MTBOX + "3";
		public static final String HTML_INDEX_MTBOX_4 = HTML_INDEX_MTBOX + "4";
		public static final String HTML_INDEX_MTBOX_5 = HTML_INDEX_MTBOX + "5";

		/**
		 * 主页纵向监控数据
		 */
		public static final String HTML_INDEX_DS = "html_index_ds_";
		public static final String HTML_INDEX_DS_ACTIVE_ROOMS = HTML_INDEX_DS + "active_rooms";
		public static final String HTML_INDEX_DS_ACTIVE_MEMBERS = HTML_INDEX_DS + "active_members";

		/**
		 * 主页窗口数据
		 */
		public static final String HTML_INDEX_WIN_ = "html_index_win_";
		public static final String HTML_INDEX_WIN_TAOBA = "html_index_win_taoba";

		/**
		 * 主页条形图数据
		 */
		public static final String HTML_INDEX_BAR = "html_index_bar";

		public static final String TAOBA_MONITOR_ = "taoba_monitor_";

	}

	/*
	 * 酷Q
	 */
	public static final String COMMAND_REPLAY_HELP = "请使用 -help/-h/-帮助 指令来获取详细操作。";
	public static final String[] ENABLE_COMMAND_MANAGER = new String[] { "!", "-" };

	public class Command {
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
		public static final String VERSION = "version(v/版本) | 无 | 获取版本。如：-v。";
		public static final String SET_ROOM_MONITOR = "setRoomMonitor(setrm/设置房间监控) | [成员名字] [关键字] | 设置口袋房间监控。在群聊使用该指令则消息发向群，在好友聊天使用该指令则消息发向好友。多个关键字使用英文逗号分割，无关键字则全量发送，否则只有包含关键字的消息才会发送。如：-setrm 张语格。";
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

	public class MonitorType {
		/**
		 * 已开启监控
		 */
		public static final int OPEN = 1;

		/**
		 * 未开启监控
		 */
		public static final int CLOS = 2;

		/**
		 * 口袋房间不存在
		 */
		public static final int NOTHING = 404;
	}

	public class MsgSend {
		public static final int NOT_SEND = 1;
		public static final int SENDED = 2;
	}

	public class TripType {
		public static final int BIRTHDAY = 0;
		public static final int LIVE = 1;
		public static final int BIRTHDAY_DINE = 3;
		public static final int PROGRAM = 5;
	}

	public class ResourceType {
		public static final String MENU = "menu";
		public static final String BUTTON = "button";
	}

	public class RoleType {
		public static final String SYSTEM_ADMIN = "systemadmin";
		public static final String YYH_DELEGATE = "delegate";
		public static final String GENERAL = "general";
	}

	public class UserState {
		public static final int NOT_ACTIVE = 0;
		public static final int NORMAL = 1;
		public static final int LOCKING = 2;
	}

	public class ModianStatus {
		public static final String COLLECTING = "众筹中";
		public static final String FINISHED = "已结束";
	}
}
