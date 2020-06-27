package com.snh48.picq.service;

import java.util.List;

import com.snh48.picq.entity.snh48.Member;

/**
 * SNH48成员服务
 * 
 * @author shiro
 *
 */
public interface MemberService {

	/**
	 * 获取缓存中的成员信息
	 * 
	 * @param roomId 口袋房间ID
	 * @return {@link Member}
	 */
	Member getCacheByRoomId(Long roomId);

	/**
	 * 获取成员列表
	 * 
	 * @param monitorType 监控状态
	 * @return {@link List<Member>}
	 */
	List<Member> getListByMonitorType(int monitorType);

	/**
	 * 获取成员列表
	 * 
	 * @param monitorType 监控状态
	 * @return {@link List<Member>}
	 */
	List<Member> getListByNotMonitorTypeNot(int monitorType);

}
