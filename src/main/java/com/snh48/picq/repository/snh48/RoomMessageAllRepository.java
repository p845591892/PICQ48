package com.snh48.picq.repository.snh48;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.snh48.picq.entity.snh48.RoomMessageAll;

/**
 * 口袋48房间留言板消息表DAO组件
 * 
 * @author shiro
 *
 */
@Repository
public interface RoomMessageAllRepository
		extends JpaRepository<RoomMessageAll, String>, JpaSpecificationExecutor<RoomMessageAll> {

	/**
	 * 获取留言板中发送过消息的用户ID集合。
	 */
	@Query(value = "select distinct t.senderUserId from RoomMessageAll t")
	List<Long> getSenderUserIdList();

	/**
	 * 获取留言板中发送过消息，并且不符合用户信息版本号的用户ID集合。
	 * 
	 * @param version 用户信息版本
	 */
	@Query(value = "SELECT DISTINCT t.senderUserId FROM RoomMessageAll t WHERE t.senderUserId NOT IN ("
			+ "SELECT y.id FROM PocketUser y WHERE y.version=:version)")
	List<Long> getSenderUserIdListByVersion(@Param(value = "version") long version);

	/**
	 * 获取指定口袋48房间的最新一条数据。
	 * 
	 * @return 消息实体对象
	 */
	RoomMessageAll findFirstByRoomIdOrderByMessageTimeDesc(Long roomId);

}
