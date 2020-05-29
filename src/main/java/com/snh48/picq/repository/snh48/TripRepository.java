package com.snh48.picq.repository.snh48;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.snh48.picq.entity.snh48.Trip;

/**
 * SNH48 Group行程表仓库
 * 
 * @author shiro
 *
 */
@Repository
public interface TripRepository extends JpaRepository<Trip, Long>, JpaSpecificationExecutor<Trip> {

	/**
	 * 升序查询行程列表。
	 * 
	 * @param type     行程类型
	 * @param showTime 最早演出时间
	 * @return 行程List集合
	 */
	List<Trip> findByTypeAndShowTimeAfterOrderByShowTimeAsc(int type, Date showTime);

	/**
	 * 升序查询行程列表。
	 * 
	 * @param type            行程类型
	 * @param locationKeyword 地址关键字
	 * @param showTime        最早演出时间
	 * @return 行程List集合
	 */
	List<Trip> findByTypeAndLocationKeywordAndShowTimeAfterOrderByShowTimeAsc(int type, String locationKeyword,
			Date showTime);

	/**
	 * 升序查询行程列表。
	 * 
	 * @param showTime 最早演出时间
	 * @return 行程List集合
	 */
	List<Trip> findByShowTimeAfterOrderByShowTimeAsc(Date showTime);

	/**
	 * 升序查询行程列表。
	 * 
	 * @param beginTime 区间开始时间
	 * @param endTime   区间结束时间
	 * @return 行程List集合
	 */
	List<Trip> findByShowTimeAfterAndShowTimeBeforeOrderByShowTimeAsc(Date beginTime, Date endTime);

	/**
	 * 升序查询行程列表。
	 * 
	 * @param locationKeyword 地址关键字
	 * @param beginTime       区间开始时间
	 * @param endTime         区间结束时间
	 * @return 行程List集合
	 */
	List<Trip> findByLocationKeywordAndShowTimeAfterAndShowTimeBeforeOrderByShowTimeAsc(String locationKeyword,
			Date beginTime, Date endTime);

	/**
	 * 升序查询行程列表。
	 * 
	 * @param type      行程类型
	 * @param beginTime 区间开始时间
	 * @param endTime   区间结束时间
	 * @return 行程List集合
	 */
	List<Trip> findByTypeAndShowTimeAfterAndShowTimeBeforeOrderByShowTimeAsc(int type, Date beginTime, Date endTime);

	/**
	 * 升序查询行程列表。
	 * 
	 * @param type            行程类型
	 * @param locationKeyword 地址关键字
	 * @param beginTime       区间开始时间
	 * @param endTime         区间结束时间
	 * @return 行程List集合
	 */
	List<Trip> findByTypeAndLocationKeywordAndShowTimeAfterAndShowTimeBeforeOrderByShowTimeAsc(int type,
			String locationKeyword, Date beginTime, Date endTime);

}
