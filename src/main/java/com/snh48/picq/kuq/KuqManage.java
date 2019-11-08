package com.snh48.picq.kuq;

import com.snh48.picq.core.QQType;
import com.snh48.picq.entity.QQCommunity;
import com.snh48.picq.entity.snh48.Member;
import com.snh48.picq.utils.DateUtil;

import cc.moecraft.icq.PicqBotX;
import cc.moecraft.icq.sender.IcqHttpApi;
import cc.moecraft.icq.sender.message.MessageBuilder;
import cc.moecraft.icq.sender.message.components.ComponentImage;
import lombok.extern.log4j.Log4j2;

/**
 * 酷Q管理类
 * 
 * @author shiro
 *
 */
@Log4j2
public class KuqManage {

	/**
	 * 发送同步的消息到QQ
	 * 
	 * @param bot         {@link PicqBotX}机器人实例
	 * @param message     格式化后的消息
	 * @param qqCommunity QQ对象
	 * @throws InterruptedException
	 */
	public static void sendSyncMessage(PicqBotX bot, String message, QQCommunity qqCommunity)
			throws InterruptedException {
		IcqHttpApi icqHttpApi = bot.getAccountManager().getNonAccountSpecifiedApi();
		sendSyncMessage(icqHttpApi, message, qqCommunity);
	}

	/**
	 * 发送同步的消息到QQ
	 * 
	 * @param icqHttpApi  icq接口
	 * @param message     格式化后的消息
	 * @param qqCommunity QQ对象
	 * @throws InterruptedException
	 */
	public static void sendSyncMessage(IcqHttpApi icqHttpApi, String message, QQCommunity qqCommunity)
			throws InterruptedException {
		boolean autoEscape = true;// 默认纯文本发送
		long qq = qqCommunity.getId();// QQ(群)号
		QQType qqType = qqCommunity.getQqType();// QQ(群)号类型

		if (message.contains("<img>") || message.contains("<audio>")) {// 当消息中含有非文字部分的时候
			autoEscape = false;
		}

		switch (qqType) {
		case GROUP:// 发送到QQ群
			icqHttpApi.sendGroupMsg(qq, syncMessageBuilder(icqHttpApi, message), autoEscape);
			break;
		case FRIEND:// 发送到QQ好友
			icqHttpApi.sendPrivateMsg(qq, syncMessageBuilder(icqHttpApi, message), autoEscape);
			break;
		default:
			log.error("QQ号[{}]的类型错误。", qqCommunity.getId());
			break;
		}

		Thread.sleep(1000);
	}

	/**
	 * 构造同步的消息。将标签与组件相转换。
	 * 
	 * @param icqHttpApi icq接口
	 * @param message    原消息
	 * @return 符合酷Q的消息字符串
	 */
	private static String syncMessageBuilder(IcqHttpApi icqHttpApi, String message) {
		String[] imgUrl = null;// 图片

		// 判断和拆分图片
		if (message.contains("<img>")) {
			imgUrl = message.split("<img>");// 分割
			message = imgUrl[0];// 图片都在正文后，因此第一条一定是正文
		}

		// 输入文字
		MessageBuilder mb = new MessageBuilder().add(message);

		// 输入图片内容
		if (imgUrl != null && imgUrl.length >= 2 && icqHttpApi.canSendImage()) {
			for (int i = 1; i < imgUrl.length; i++) {// 0为文字部分，故从1开始
				String url = imgUrl[i];
				mb.newLine().add(new ComponentImage(url));
			}
		}

		return mb.toString();
	}

	/**
	 * 构造成员实体类的消息。
	 * 
	 * @param member 成员对象
	 * @return 转化后的字符串
	 */
	public static String memberMessageBuilder(Member member) {
		MessageBuilder mb = new MessageBuilder();
		mb.add(new ComponentImage(member.getAvatar()));
		mb.newLine().add("名字：").add(member.getName() + "(" + member.getPinyin() + ")");
		mb.newLine().add("昵称：").add(member.getNickname());
		mb.newLine().add("生日：").add(member.getBirthday() + "(" + member.getConstellation() + ")");
		mb.newLine().add("出生地：").add(member.getBirthplace());
		mb.newLine().add("血型：").add(member.getBloodType());
		mb.newLine().add("身高：").add(member.getHeight());
		mb.newLine().add("所属：").add(member.getGroupName() + " " + member.getTeamName());
		mb.newLine().add("加入日期：").add(DateUtil.getDate(member.getJoinTime(), "yyyy-MM-dd"));
		mb.newLine().add("爱好：").add(member.getHobbies());
		mb.newLine().add("特长：").add(member.getSpecialty());
		mb.newLine().add("口袋房间名：").add(member.getRoomName());
		mb.newLine().add("口袋房间话题：").add(member.getTopic());
		mb.newLine().add("监控状态：")
				.add(member.getRoomMonitor() == 1 ? "监控中" : member.getRoomMonitor() == 2 ? "未监控" : "房间已关闭");
		return mb.toString();
	}

}
