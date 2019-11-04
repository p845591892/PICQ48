package com.snh48.picq.https;

/**
 * Https请求相关的通用参数。
 * 
 * @author shiro
 *
 */
public class HttpsURL {

	/**
	 * 发送请求的间隔时间
	 */
	public static final long REQUEST_INTERVAL_TIME = 5000l;

	/**
	 * 图片 视频等域名地址
	 */
	public static final String SOURCE = "https://source.48.cn";

	/**
	 * 48网H5域名地址
	 */
	public static final String H5_48 = "https://h5.48.cn";

	/**
	 * 口袋48接口域名地址
	 */
	public static final String POCKET_API = "https://pocketapi.48.cn";

	/**
	 * SNH48 Group全体成员列表接口。 请求方式为GET。
	 */
	public static final String ALL_MEMBER_LIST = H5_48 + "/memberPage/member_mapping.json";

	/**
	 * SNH48 Group成员口袋房间生放送地址
	 */
	public static final String LIVE = H5_48 + "/2019appshare/memberLiveShare/?id=";

	/**
	 * SNH48成员个人信息接口。请求方式为POST。
	 */
	public static final String MEMBER = POCKET_API + "/user/api/v1/user/star/archives";

	/**
	 * 登录口袋48接口。请求方式为POST。
	 */
	public static final String TOKEN = POCKET_API + "/user/api/v1/login/app/mobile";

	/**
	 * SNH48成员的口袋48房间信息接口。请求方式为POST。
	 */
	public static final String MEMBER_ROOM = POCKET_API + "/im/api/v1/im/room/info/type/source";

	/**
	 * SNH48成员的口袋房间消息接口。请求方式为POST。
	 */
	public static final String ROOM_MESSAGE = POCKET_API + "/im/api/v1/chatroom/msg/list/homeowner";

	/**
	 * SNH48成员的口袋房间翻牌消息接口。请求方式为POST。
	 */
	public static final String ROOM_MESSAGE_FLIPCARD = POCKET_API
			+ "/idolanswer/api/idolanswer/v1/question_answer/detail";

	/**
	 * 微博数据接口。请求方式为GET。
	 */
	public static final String WEIBO = "https://m.weibo.cn/api/container/getIndex";

}
