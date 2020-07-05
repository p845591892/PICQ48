package com.snh48.picq.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * @Description: 首页mtbox的VO类
 * @author: JuFF_白羽
 * @date: 2018年9月23日 下午9:19:05
 */
@Data
public class MtboxVO implements Serializable{

	private static final long serialVersionUID = -1677992089631327909L;

	private String name;

	private Object data;

	private String icon;

	private Integer count;
	
	private Object date;
	
}
