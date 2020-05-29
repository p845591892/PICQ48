package com.snh48.picq.kuq;

import java.io.File;
import java.util.Date;

import com.snh48.picq.config.KuqProperties;
import com.snh48.picq.core.QQType;
import com.snh48.picq.entity.QQCommunity;
import com.snh48.picq.entity.snh48.Member;
import com.snh48.picq.entity.snh48.Trip;
import com.snh48.picq.utils.DateUtil;
import com.snh48.picq.utils.Https;
import com.snh48.picq.utils.SpringUtil;

import cc.moecraft.icq.PicqBotX;
import cc.moecraft.icq.sender.IcqHttpApi;
import cc.moecraft.icq.sender.message.MessageBuilder;
import cc.moecraft.icq.sender.message.components.ComponentImage;
import cc.moecraft.icq.sender.message.components.ComponentRecord;
import lombok.extern.log4j.Log4j2;

/**
 * 酷Q管理类
 * 
 * @author shiro
 *
 */
@Log4j2
public class KuqManage {

	public static void sendMessage(long groupId, String message, boolean autoEscape, QQType qqType) {

	}

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
		boolean autoEscape = true;// 默认非纯文本发送
		long qq = qqCommunity.getId();// QQ(群)号
		QQType qqType = qqCommunity.getQqType();// QQ(群)号类型

		if (message.contains("<img>") || message.contains("<audio>")) {// 当消息中含有非文字部分的时候
			autoEscape = false;
			message = messageBuilder(icqHttpApi, message);
		}

		switch (qqType) {
		case GROUP:// 发送到QQ群
			try {
				icqHttpApi.sendGroupMsg(qq, message, autoEscape);
			} catch (Exception e) {
				log.info("发送同步消息到Q群发生异常：{}", e.getMessage());
			}
			break;
		case FRIEND:// 发送到QQ好友
			try {
				icqHttpApi.sendPrivateMsg(qq, message, autoEscape);
			} catch (Exception e) {
				log.info("发送同步消息到好友发生异常：{}", e.getMessage());
			}
			break;
		default:
			log.error("QQ号[{}]的类型错误。", qqCommunity.getId());
			break;
		}

		Thread.sleep(1000);
	}

	/**
	 * 构造含有非文字的消息。将标签与组件相转换。
	 * 
	 * @param icqHttpApi icq接口
	 * @param message    原消息
	 * @return 符合酷Q的消息字符串
	 */
	public static String messageBuilder(IcqHttpApi icqHttpApi, String message) {
		String[] imgUrl = null;// 图片
		String[] audioUrl = null;// 语音

		/* 处理图片URL */
		if (message.contains("<img>")) {
			imgUrl = message.split("<img>");// 分割
			message = imgUrl[0];// 图片都在正文后，因此第一条一定是正文
		}

		/* 处理语音URL */
		if (message.contains("<audio>")) {
			audioUrl = message.split("<audio>");// 分割
			message = audioUrl[0];// 语音都在[1]
		}

		// 构造消息对象
		MessageBuilder mb = new MessageBuilder();

		// 输入图片内容
		if (imgUrl != null && imgUrl.length >= 2 && icqHttpApi.canSendImage()) {
			mb.add(message);

			for (int i = 1; i < imgUrl.length; i++) {// 0为文字部分，故从1开始
				String url = imgUrl[i];
				if (i == 1) {
					mb.add(new ComponentImage(url));
				} else {
					mb.newLine().add(new ComponentImage(url));
				}
			}
			return mb.toString();
		}

		// 输入语音内容
		if (audioUrl != null && audioUrl.length == 2 && icqHttpApi.canSendRecord()) {
			KuqProperties properties = SpringUtil.getBean(KuqProperties.class);
			String savePath = properties.getHomePath() + File.separator + "data" + File.separator + "record";
			String fileName = DateUtil.getDate("yyyyMMddHHmmss") + ".mp3";
			try {
				Https https = new Https();
				https.setUrl(audioUrl[1]).downloadFile(savePath, fileName);
			} catch (Exception e) {
				e.printStackTrace();
			}
			mb.add(new ComponentRecord(fileName));
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

	/**
	 * 构造行程列表消息。
	 * 
	 * @param list 行程列表
	 * @return 转化后的字符串
	 */
	public static String tripMessageBuilder(Trip trip) {
		Date showTime = trip.getShowTime();
		String title = trip.getTitle();
		String subTitle = trip.getSubTitle();
		
		MessageBuilder mb = new MessageBuilder();
		mb.add(DateUtil.getDate(showTime, "yyyy-MM-dd HH:mm"));
		mb.add(" " + trip.getTitle());
		mb.newLine();
		if (!title.equals(subTitle)) {
			mb.add(trip.getSubTitle());
			mb.newLine();
		}
		mb.add(trip.getJoinMemberName());
		mb.newLine();
		mb.add(trip.getContent());
		
		return mb.toString();
	}

}
