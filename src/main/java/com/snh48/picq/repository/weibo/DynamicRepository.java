package com.snh48.picq.repository.weibo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.snh48.picq.entity.weibo.Dynamic;

/**
 * @ClassName: DynamicRepository
 * @Description: 微博动态表DAO组件
 * @author JuFF_白羽
 * @date 2018年8月1日 上午10:44:20
 */
@Repository
public interface DynamicRepository extends JpaRepository<Dynamic, Long> {

	/**
	 * @Title: findByIsSendOrderBySyncDateAsc
	 * @Description: 根据是否发送状态获得同步时间升序的微博动态集合
	 * @author JuFF_白羽
	 * @param isSend 是否发送：1未发送，2已发送
	 * @return List<Dynamic> 微博动态集合
	 */
	List<Dynamic> findByIsSendOrderBySyncDateAsc(int isSend);

	/**
	 * @Description: 获取指定日期后的微博动态同步总量
	 * @author: JuFF_白羽
	 * @param date 指定时间
	 * @date: 2018年9月24日 上午12:40:48
	 */
	@Query("select count(t) from Dynamic t where t.syncDate >= ?1")
	Integer countGreaterDate(Date date);

}
