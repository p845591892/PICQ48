package com.snh48.picq.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.snh48.picq.core.Common;

import cc.moecraft.icq.PicqBotX;
import cc.moecraft.icq.PicqConfig;
import cc.moecraft.icq.command.interfaces.IcqCommand;
import cc.moecraft.icq.event.IcqListener;
import lombok.extern.log4j.Log4j2;

/**
 * 酷Q配置类
 * 
 * @author shiro
 *
 */
@Log4j2
@Configuration
public class KuqConfig {

	@Autowired
	private KuqProperties kuqProperties;

	@Bean
	public PicqConfig picqConfig() {
		// 创建机器人配置 ( 传入PICQ端口 )
		PicqConfig picqConfig = new PicqConfig(kuqProperties.getSocketPort());
		picqConfig.setLogInit(kuqProperties.isLogInit());
		picqConfig.setLogPath(kuqProperties.getLogPath());
		picqConfig.setDebug(true);
		return picqConfig;
	}

	@Bean
	public PicqBotX picqBotX(PicqConfig picqConfig, IcqListener[] listeners, IcqCommand[] commands) {
		// 创建机器人对象 ( 传入配置 )
		PicqBotX bot = new PicqBotX(picqConfig);

		if (!kuqProperties.isEnabled()) {// 是否启动
			return bot;
		}

		// 添加一个机器人账户 ( 名字, 发送URL, 发送端口 )
		bot.addAccount(kuqProperties.getBotName(), kuqProperties.getPostUrl(), kuqProperties.getPostPort());

		// 注册事件监听器, 可以注册多个监听器。组件存放位置 com.snh48.picq.kuq.command
		log.info("----------酷Q监听器 {} 个", listeners.length);
		bot.getEventManager().registerListeners(listeners);

		// 启用指令管理器
		// 这些字符串是指令前缀, 比如指令"!help"的前缀就是"!"
		bot.enableCommandManager(Common.ENABLE_COMMAND_MANAGER);

		// 注册指令, 可以注册多个指令。组件存放位置 com.snh48.picq.kuq.listener
		log.info("----------酷Q指令器 {} 个", commands.length);
		bot.getCommandManager().registerCommands(commands);

		bot.startBot();// 启动机器人, 不会占用主线程

		return bot;
	}

}
