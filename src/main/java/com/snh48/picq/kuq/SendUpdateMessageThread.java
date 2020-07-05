package com.snh48.picq.kuq;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.snh48.picq.config.KuqProperties;
import com.snh48.picq.core.Common.PicqSystem;
import com.snh48.picq.entity.QQCommunity;
import com.snh48.picq.repository.QQCommunityRepository;

import cc.moecraft.icq.PicqBotX;
import cc.moecraft.icq.sender.IcqHttpApi;
import cc.moecraft.icq.sender.message.MessageBuilder;
import cc.moecraft.icq.sender.returndata.returnpojo.get.RVersionInfo;
import lombok.extern.log4j.Log4j2;

/**
 * 启动发送版本信息给QQ好友/群
 * 
 * @author shiro
 *
 */
@Log4j2
@Configuration
public class SendUpdateMessageThread implements Runnable {

	@Autowired
	private QQCommunityRepository qqCommunityRepository;

	@Autowired
	private PicqBotX bot;

	@Autowired
	private KuqProperties kuqProperties;

	@Override
	public void run() {
		if (!kuqProperties.isEnabled()) {
			return;
		}

		IcqHttpApi icqHttpApi = bot.getAccountManager().getNonAccountSpecifiedApi();
		RVersionInfo ver = icqHttpApi.getVersionInfo().getData();
		MessageBuilder mb = new MessageBuilder();
		mb.add("PICQ已重启").newLine();
		mb.add(PicqSystem.UPDATE_TIME).newLine();
		mb.newLine().add("系统版本信息：");
		mb.newLine().add(PicqSystem.PICQ48_VERSION);
		mb.newLine().add(PicqSystem.PICQ_BOT_X_VERSION).newLine();
		mb.newLine().add("酷Q版本信息：");
		mb.newLine().add("【型号】  " + ver.getCoolqEdition());
		mb.newLine().add("【版本】  " + ver.getPluginVersion()).newLine();
		mb.newLine().add("主页：http://106.55.41.91:8080/index");
		mb.newLine().add("指令功能帮助请输入：-help");
		mb.newLine().add(PicqSystem.UPDATE_MESSAGE);

		List<QQCommunity> qqCommunitys = qqCommunityRepository.findAll();
		for (QQCommunity qqCommunity : qqCommunitys) {
			try {
				KuqManage.sendSyncMessage(icqHttpApi, mb.toString(), qqCommunity);
			} catch (InterruptedException e) {
				log.error("向{}发送PICQ48重启通知失败，异常：{}", qqCommunity.getCommunityName(), e.toString());
			}
		}
		
		log.info("---------发送PICQ48重启通知完毕---------");
	}

}
