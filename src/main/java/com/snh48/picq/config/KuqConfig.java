//package com.snh48.picq.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import cc.moecraft.icq.PicqBotX;
//import cc.moecraft.icq.PicqConfig;
//
//@Configuration
//public class KuqConfig {
//
//	@Autowired
//	private KuqProperties kuqProperties;
//
//	@Bean
//	public PicqConfig picqConfig() {
//		// 创建机器人配置 ( 传入Picq端口 )
//		PicqConfig picqConfig = new PicqConfig(kuqProperties.getSocketPort());
//		picqConfig.setLogPath(kuqProperties.getLogPath());
//		picqConfig.setDebug(true);
//		return picqConfig;
//	}
//
//	@Bean
//	public PicqBotX picqBotX(PicqConfig picqConfig) {
//		// 创建机器人对象 ( 传入配置 )
//		PicqBotX bot = new PicqBotX(picqConfig);
//
//		// 添加一个机器人账户 ( 名字, 发送URL, 发送端口 )
//		bot.addAccount(kuqProperties.getBotName(), kuqProperties.getPostUrl(), kuqProperties.getPostPort());
//
//		// 注册事件监听器, 可以注册多个监听器
////		bot.getEventManager().registerListeners(new EventPrivateMessageListener());
//
//		// 启用指令管理器
//		// 这些字符串是指令前缀, 比如指令"!help"的前缀就是"!"
//		bot.enableCommandManager("-robot");
//
//		// 注册指令, 可以注册多个指令
////		bot.getCommandManager().registerCommands(new SwitchSendTextToGroupsCommand());
//
//		if (kuqProperties.isEnabled()) {
//			// 启动机器人, 不会占用主线程
//			bot.startBot();
//		}
//
//		return bot;
//	}
//
//}
