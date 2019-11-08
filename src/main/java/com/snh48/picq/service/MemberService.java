package com.snh48.picq.service;

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

}
