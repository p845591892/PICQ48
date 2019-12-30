package com.snh48.picq.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 在类或者方法上使用的日志注解，记录用户的操作。类型参数由{@link OperationType
 * com.snh48.picq.annotation.OperationType}提供。
 * 
 * <p>
 * 默认操作类型为{@link OperationType.OTHER}。
 * 
 * @author shiro
 *
 */
@Documented // 文档化
@Retention(RUNTIME) // 设置存储方式
@Target(METHOD) // 设置注解生效位置
@Inherited // 注解在子类中也会生效
public @interface Log {

	/**
	 * 操作内容
	 */
	String desc();

	/**
	 * 设置操作类型
	 * <p>
	 * 默认为其他操作。
	 * 
	 * @return
	 */
	OperationType type() default OperationType.OTHER;

}
