package com.snh48.picq.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @ClassName: SpringUtil
 * @Description: Spring框架工具类
 * @author JuFF_白羽
 * @date 2018年3月19日 上午9:21:10
 */
@Component
public class SpringUtil implements ApplicationContextAware {

	private static ApplicationContext applicationContext;

	private Logger LOGGER = LoggerFactory.getLogger(SpringUtil.class);

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if (SpringUtil.applicationContext == null) {
			SpringUtil.applicationContext = applicationContext;
			LOGGER.info("ApplicationContext配置成功,在普通类可以通过调用SpringUtil调用静态方法");
		}
	}

	/**
	 * 获取applicationContext
	 */
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * 通过name获取 Bean.
	 */
	public static Object getBean(String name) {
		return getApplicationContext().getBean(name);
	}

	/**
	 * 通过class获取Bean.
	 */
	public static <T> T getBean(Class<T> clazz) {
		return getApplicationContext().getBean(clazz);
	}

	/**
	 * 通过name,以及Clazz返回指定的Bean
	 */
	public static <T> T getBean(String name, Class<T> clazz) {
		return getApplicationContext().getBean(name, clazz);
	}

}
