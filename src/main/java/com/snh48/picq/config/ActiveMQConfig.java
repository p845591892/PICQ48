package com.snh48.picq.config;

import javax.jms.ConnectionFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;

/**
 * 消息队列配置类
 * 
 * @author shiro
 *
 */
@Configuration
public class ActiveMQConfig {

	@Bean
	public JmsListenerContainerFactory<?> topicListenerContainerFactory(ConnectionFactory connectionFactory) {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		factory.setPubSubDomain(true);
		factory.setConnectionFactory(connectionFactory);
		return factory;
	}

}
