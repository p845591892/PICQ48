package com.snh48.picq.vo;

/**
 * @ClassName: ResultVO
 * @Description: 接口返回结果类
 * @author JuFF_白羽
 * @date 2018年7月23日 上午10:31:11
 */
public class ResultVO {

	/**
	 * 状态码
	 */
	private Integer status;

	/**
	 * 原因
	 */
	private String cause;

	/**
	 * 数据
	 */
	private Object data;
	
	public ResultVO() {
	}
	
	public ResultVO(Integer status, String cause) {
		this.status = status;
		this.cause = cause;
	}
	
	public ResultVO(Integer status, String cause, Object data) {
		this.status = status;
		this.cause = cause;
		this.data = data;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
