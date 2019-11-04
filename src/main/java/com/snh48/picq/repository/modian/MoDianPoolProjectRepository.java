package com.snh48.picq.repository.modian;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.snh48.picq.entity.modian.MoDianPoolProject;

/**
 * @ClassName: MoDianPoolProjectRepository
 * @Description: 摩点集资项目表DAO组件
 * @author JuFF_白羽
 * @date 2018年8月6日 下午5:27:49
 */
@Repository
public interface MoDianPoolProjectRepository extends JpaRepository<MoDianPoolProject, Long> {

	/**
	 * @Title: findByEndTimeGreaterThanEqualsOrEndTimeIsNull
	 * @Description: 获取指定状态或者状态为空的摩点项目
	 * @author JuFF_白羽
	 * @param status 项目状态
	 * @return List<MoDianPoolProject> 摩点项目集合
	 */
	List<MoDianPoolProject> findByStatusOrStatusIsNull(String status);

	/**
	 * @Title: findByStatus
	 * @Description: 根据项目状态获取摩点项目
	 * @author JuFF_白羽
	 * @param status 项目状态
	 * @return List<MoDianPoolProject> 摩点项目集合
	 */
	List<MoDianPoolProject> findByStatus(String status);

	/**
	 * @Description: 根据项目状态获取摩点项目数量
	 * @param status 众筹中/已结束
	 * @author: JuFF_白羽
	 * @date: 2018年9月24日 上午12:08:52
	 */
	@Query("select count(t) from MoDianPoolProject t where t.status = ?1")
	Integer countByStatus(String status);

	/**
	 * @Description: 根据项目结束时间降序获得摩点项目
	 * @author JuFF_白羽
	 * @return 摩点项目列表
	 */
	@Query("from MoDianPoolProject t order by t.endTime desc")
	List<MoDianPoolProject> findOrderByEndTimeDesc();

}
