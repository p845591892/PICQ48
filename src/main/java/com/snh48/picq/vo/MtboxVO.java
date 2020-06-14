package com.snh48.picq.vo;

import java.io.Serializable;

/**
 * @Description: 首页mtbox的VO类
 * @author: JuFF_白羽
 * @date: 2018年9月23日 下午9:19:05
 */
public class MtboxVO implements Serializable{

	private static final long serialVersionUID = -1677992089631327909L;

	private String name;

	private Object data;

	private String icon;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

}
