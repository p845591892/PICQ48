package com.snh48.picq.annotation;

import java.lang.reflect.Method;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.subject.Subject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;
import com.snh48.picq.entity.system.SystemLog;
import com.snh48.picq.repository.system.SystemLogRepository;
import com.snh48.picq.shiro.Principal;
import com.snh48.picq.utils.IpUtil;
import com.snh48.picq.utils.StringUtil;

/**
 * 日志的AOP类，用于记录被注解{@link Log com.snh48.picq.annotation.Log}标注的方法的调用记录。
 * 
 * <p>
 * 主要用于web接口，对用户的增删改查操作记录做持久化。
 * 
 * @author shiro
 *
 */
@Aspect // AOP声明
@Component
public class LogAspect {

	@Autowired
	private SystemLogRepository systemLogRepository;

	/**
	 * 声明切面为被注解{@link Log com.snh48.picq.annotation.Log}标注的方法。
	 */
	@Pointcut("@annotation(com.snh48.picq.annotation.Log)")
	private void pointcut() {
	}

	/**
	 * 操作成功时进行日志记录。
	 * 
	 * @param joinPoint 连接点
	 */
	@After(value = "pointcut()")
	public void afterSuccess(JoinPoint joinPoint) {
		addSuccessLog(joinPoint);
	}

	/**
	 * 操作发生异常时进行日志记录。
	 * 
	 * @param joinPoint 连接点
	 */
	@AfterThrowing(value = "pointcut()", throwing = "e")
	public void afterThrowing(JoinPoint joinPoint, Exception e) {
		addThrowingLog(joinPoint, e);
	}

	/**
	 * 新增正常日志。
	 * 
	 * @param joinPoint 连接点
	 */
	private void addSuccessLog(JoinPoint joinPoint) {
		addLog(joinPoint, true, null);
	}

	/**
	 * 新增异常日志。
	 * 
	 * @param joinPoint 连接点
	 * @param e         异常
	 */
	private void addThrowingLog(JoinPoint joinPoint, Exception e) {
		addLog(joinPoint, false, e);
	}

	/**
	 * 新增日志。
	 * 
	 * @param joinPoint   连接点
	 * @param operSuccess 操作是否成功
	 * @param e           异常
	 */
	private void addLog(JoinPoint joinPoint, boolean operSuccess, Exception e) {
		OperationType operType = getType(joinPoint);
		StringBuffer operDescBuffer = new StringBuffer(getDesc(joinPoint));
		String param = getParam(joinPoint);
		SystemLog sysLog = new SystemLog();// 声明日志实例

		/* request相关监控 */
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		if (requestAttributes != null) {
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
					.getRequest();
			String ip = IpUtil.getIpAddr(request);
			operDescBuffer.append("：");
			operDescBuffer.append(request.getRequestURI());
			sysLog.setIp(ip);// 设置访问IP
		}
		sysLog.setOperDesc(operDescBuffer.toString());// 设置操作内容
		sysLog.setOperType(operType);// 设置操作类型
		sysLog.setOperSuccess(operSuccess);// 设置操作是否成功
		sysLog.setCreateTime(new Date());// 设置创建时间
		/* 异常信息监控 */
		if (!operSuccess && e != null) {
			sysLog.setSuppContent(e.toString());// 设置异常信息
		}
		/* 携带参数监控 */
		if (StringUtil.isNotBlank(param)) {
			sysLog.setParam(param);// 设置参数信息
		}
		/* 操作人监控 */
		Subject subject = Principal.getSubject();
		if (subject.isAuthenticated()) {
			long uid = Principal.getCurrentUse().getId();
			sysLog.setUid(uid);// 设置操作人ID
		} else {
			sysLog.setUid(0l);// 设置操作人ID
		}

		systemLogRepository.save(sysLog);
	}

	/**
	 * 获取注解{@link Log com.snh48.picq.annotation.Log}中设置的操作内容。
	 * 
	 * @param joinPoint 连接点
	 * @return 返回操作内容字符串
	 */
	private String getDesc(JoinPoint joinPoint) {
		Method method = getMethod(joinPoint);
		return method.getAnnotation(Log.class).desc();
	}

	/**
	 * 获取注解{@link Log com.snh48.picq.annotation.Log}中设置的操作类型。
	 * 
	 * @param joinPoint 连接点
	 * @return 返回操作类型的枚举
	 */
	private OperationType getType(JoinPoint joinPoint) {
		Method method = getMethod(joinPoint);
		return method.getAnnotation(Log.class).type();
	}

	/**
	 * 获取切入的的方法实例。
	 * 
	 * @param joinPoint 连接点
	 * @return 返回方法实例
	 */
	private Method getMethod(JoinPoint joinPoint) {
		MethodSignature methodName = (MethodSignature) joinPoint.getSignature();
		Method method = methodName.getMethod();
		return method;
	}

	/**
	 * 获取请求携带的参数。
	 * 
	 * @param joinPoint 连接点
	 * @return 参数处理拼接后的字符串
	 */
	private String getParam(JoinPoint joinPoint) {
		Object[] obj = joinPoint.getArgs();
		StringBuffer buffer = new StringBuffer();
		if (obj != null) {
			for (int i = 0; i < obj.length; i++) {
				buffer.append("[参数" + (i + 1) + ":");
				Object o = obj[i];
				if (o instanceof Model) {
					continue;
				}
				String parameter = null;
				try {
					parameter = JSON.toJSONString(o);
				} catch (Exception e) {
					continue;
				}
				buffer.append(parameter);
				buffer.append("]");
			}
		}
		return buffer.toString();
	}

}
