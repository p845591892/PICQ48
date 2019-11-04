package com.snh48.picq.dao;

import org.apache.ibatis.annotations.Mapper;

import com.snh48.picq.entity.snh48.Member;

/**
 * SNH48成员信息表DAO组件
 * 
 * @author shiro
 *
 */
@Mapper
public interface MemberDao {

	/**
	 * 根据id更新成员信息
	 * <p>
	 * 更新现有成员的，有变动可能的资料字段，当字段roomMonitor为空时默认不修改。
	 * <p>
	 * 参数字段： avatar, teamId, teamName, groupId, groupName, roomMonitor, roomName,
	 * topic.
	 * 
	 * @param member 成员实体类com.gnz48.zzt.entity.snh48.Member
	 * @return 受影响的数据行数
	 */
	int updateMemberById(Member member);

}
