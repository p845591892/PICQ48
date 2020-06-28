package com.snh48.picq.repository.taoba;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.snh48.picq.entity.taoba.TaobaMonitor;
import com.snh48.picq.vo.TaobaMonitorVO;

/**
 * 桃叭项目监控配置表仓库组件
 * 
 * @author shiro
 *
 */
@Repository
public interface TaobaMonitorRepository
		extends JpaRepository<TaobaMonitor, Long>, JpaSpecificationExecutor<TaobaMonitor> {

	@Query(value = "select new com.snh48.picq.vo.TaobaMonitorVO(t,q) from TaobaMonitor t, com.snh48.picq.entity.QQCommunity q where t.communityId = q.id and t.detailId = ?1")
	List<TaobaMonitorVO> findTaobaMonitorAndQQCommunityByDetailId(long detailId);

	@Transactional
	@Modifying
	@Query("delete from TaobaMonitor t where t.detailId = ?1")
	int deleteByDetailId(long id);

}
