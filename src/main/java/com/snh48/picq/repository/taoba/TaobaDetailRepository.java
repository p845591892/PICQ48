package com.snh48.picq.repository.taoba;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.snh48.picq.entity.taoba.TaobaDetail;

/**
 * 桃叭项目表仓库
 * 
 * @author shiro
 *
 */
@Repository
public interface TaobaDetailRepository extends JpaRepository<TaobaDetail, Long>, JpaSpecificationExecutor<TaobaDetail> {

	/**
	 * 获取桃叭项目列表
	 * 
	 * @param running 进行状态
	 * @return {@link TaobaDetail}集合
	 */
	List<TaobaDetail> findByRunning(boolean running);

	/**
	 * 获取桃叭项目列表
	 * 
	 * @param running 进行状态
	 * @return {@link TaobaDetail}集合
	 */
	List<TaobaDetail> findByRunningNot(boolean running);

}
