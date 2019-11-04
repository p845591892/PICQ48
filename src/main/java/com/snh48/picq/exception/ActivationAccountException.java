package com.snh48.picq.exception;

import org.apache.shiro.authc.AccountException;

/**
 * @Description: 账号激活异常
 *               <p>
 *               账号注册后需要由管理员手动激活，未激活的账号不允许登录，并且由Realm抛出该异常。
 * @author JuFF_白羽
 * @date 2018年12月18日 下午5:01:30
 */
@SuppressWarnings("serial")
public class ActivationAccountException extends AccountException {
	/**
	 * Creates a new ActivationAccountException.
	 */
	public ActivationAccountException() {
		super();
	}

	/**
	 * Constructs a new ActivationAccountException.
	 *
	 * @param message
	 *            the reason for the exception
	 */
	public ActivationAccountException(String message) {
		super(message);
	}

	/**
	 * Constructs a new ActivationAccountException.
	 *
	 * @param cause
	 *            the underlying Throwable that caused this exception to be
	 *            thrown.
	 */
	public ActivationAccountException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructs a new ActivationAccountException.
	 *
	 * @param message
	 *            the reason for the exception
	 * @param cause
	 *            the underlying Throwable that caused this exception to be
	 *            thrown.
	 */
	public ActivationAccountException(String message, Throwable cause) {
		super(message, cause);
	}
}
