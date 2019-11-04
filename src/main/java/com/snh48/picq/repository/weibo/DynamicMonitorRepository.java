package com.snh48.picq.repository.weibo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.snh48.picq.entity.weibo.DynamicMonitor;
import com.snh48.picq.vo.DynamicMonitorVO;

/**
 * @ClassName: DynamicMonitorRepository
 * @Description: 微博动态监控配置表DAO组件
 * @author JuFF_白羽
 * @date 2018年8月1日 上午11:48:38
 */
@Repository
public interface DynamicMonitorRepository extends JpaRepository<DynamicMonitor, Long> {

	/**
	 * @Title: findDynamicMonitorAndQQCommunityByUserId
	 * @Description: 根据动态的用户ID获取微博动态监控配置
	 * @author JuFF_白羽
	 * @param userId 微博用户ID
	 * @return List<DynamicMonitorVO> 微博动态监控配置VO类集合
	 */
	@Query(value = "select new com.snh48.picq.vo.DynamicMonitorVO(d,q) from DynamicMonitor d, com.snh48.picq.entity.QQCommunity q where d.communityId = q.id and d.userId = ?1")
	List<DynamicMonitorVO> findDynamicMonitorAndQQCommunityByUserId(Long userId);
	
	/**
	 * @Title: deleteByCommunityId
	 * @Description: 删除QQ号对应的配置
	 * @author JuFF_白羽
	 * @param communityId QQ号
	 * @return int 受影响的行数
	 */
	@Transactional
	@Modifying
	@Query("delete from DynamicMonitor t where t.communityId = ?1")
	int deleteByCommunityId(long communityId);

	/**
	 * @Description: 删除监控微博对应的配置
	 * @author JuFF_白羽
	 * @param userId 微博用户ID
	 * @return int 受影响的行数
	 */
	@Transactional
	@Modifying
	@Query("delete from DynamicMonitor t where t.userId = ?1")
	int deleteByUserId(long userId);

}
