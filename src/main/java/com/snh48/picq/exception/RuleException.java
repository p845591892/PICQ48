package com.snh48.picq.exception;

/**
 * 
 * Copyright: Copyright (c) 2018 LanRu-Caifu
 * 
 * @ClassName: RuleException.java
 * @Description: 异常类
 *
 * @version: v1.0.0
 * @author: JuFF_白羽
 * @date: 2018年5月29日 下午9:52:01
 *
 *        Modification History: Date Author Version Description
 *        ---------------------------------------------------------*
 * 
 */
@SuppressWarnings("serial")
public class RuleException extends RuntimeException {

	public RuleException() {
		super();
	}

	public RuleException(String message, Throwable cause) {
		super(message, cause);
	}

	public RuleException(String message) {
		super(message);
	}

	public RuleException(Throwable cause) {
		super(cause);
	}

}
