package com.snh48.picq.annotation;

/**
 * 为日志注解 {@link Log com.snh48.picq.annotation.Log} 提供类型参数。
 * 
 * <p>
 * 包括 {@link #ADD}，{@link #UPDATE}，{@link #DEL}，{@link #SELECT}，{@link #OTHER}
 * 这些类型。为日志注解提供操作类型，并存入数据库中。
 * 
 * @author shiro
 *
 */
public enum OperationType {

	/** 新增 */
	ADD,

	/** 修改 */
	UPDATE,

	/** 删除 */
	DEL,

	/** 查询 */
	SELECT,

	/** 登录 */
	LOGIN,
	
	/** 注册 */
	REGISTER,

	/** 其他 */
	OTHER;

}
