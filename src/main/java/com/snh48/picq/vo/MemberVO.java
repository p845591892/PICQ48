package com.snh48.picq.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * 成员表VO类
 * 
 * @author shiro
 */
@Data
public class MemberVO implements Serializable {

	private static final long serialVersionUID = -133268782764501682L;

	private String groupName;
	private String teamName;
	private String searchText;
	private String name;
	private String abbr;
	private String roomMonitor;

}
