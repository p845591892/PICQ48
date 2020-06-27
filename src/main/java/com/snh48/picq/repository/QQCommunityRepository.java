package com.snh48.picq.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.snh48.picq.entity.QQCommunity;

/**
 * @ClassName: QQCommunityRepository
 * @Description: （yyh）QQ群信息表DAO组件
 * @author JuFF_白羽
 * @date 2018年7月25日 下午4:45:47
 */
@Repository
public interface QQCommunityRepository extends JpaRepository<QQCommunity, Long> {

	/**
	 * @Description: 根据房间ID获取未监控该房间的QQ列表
	 * @author JuFF_白羽
	 * @param roomId
	 *            房间ID
	 */
	@Query(value = "from QQCommunity t where t.id not in (select r.communityId from RoomMonitor r where r.roomId = ?1)")
	List<QQCommunity> findByNotInIdAndRoomId(Long roomId);

	/**
	 * 
	 * @Description: 根据摩点项目ID获取未监控该项目的QQ列表
	 * @author JuFF_白羽
	 * @param projectId
	 *            摩点项目ID
	 */
	@Query(value = "from QQCommunity t where t.id not in (select r.communityId from CommentMonitor r where r.projectId = ?1)")
	List<QQCommunity> findByNotInIdAndProjectId(Long projectId);

	/**
	 * 
	 * @Description: 根据微博用户ID获取未监控该动态的QQ列表
	 * @author JuFF_白羽
	 * @param userId
	 *            微博用户ID
	 */
	@Query(value = "from QQCommunity t where t.id not in (select r.communityId from DynamicMonitor r where r.userId = ?1)")
	List<QQCommunity> findByNotInIdAndUserId(Long userId);

	/**
	 * 根据桃叭项目ID获取未监控该项目的QQ列表
	 * @param detailId 桃叭项目ID
	 */
	@Query(value = "from QQCommunity t where t.id not in (select r.communityId from TaobaMonitor r where r.id = ?1)")
	List<QQCommunity> findByNotInIdAndDetailId(Long detailId);

}
