package com.snh48.picq.repository.taoba;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.snh48.picq.entity.taoba.TaobaJoin;

/**
 * 桃叭集资详细表仓库组件
 * 
 * @author shiro
 *
 */
@Repository
public interface TaobaJoinRepository extends JpaRepository<TaobaJoin, Long>, JpaSpecificationExecutor<TaobaJoin> {

	/**
	 * 获取指定项目最新的一条集资详细对象
	 * 
	 * @param detailId 所属项目ID
	 * @return {@link TaobaJoin}
	 */
	TaobaJoin findFirstByDetailIdOrderByCreatTimeDesc(long detailId);

	@Transactional
	@Modifying
	@Query("delete from TaobaJoin t where t.detailId = ?1")
	int deleteByDetailId(long id);

}
