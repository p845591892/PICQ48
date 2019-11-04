package com.snh48.picq.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.snh48.picq.entity.QuartzConfig;

/**
 * @ClassName: QuartzConfigRepository
 * @Description: 定时任务配置Repository
 * @author JuFF_白羽
 * @date 2018年3月16日 下午3:14:37
 */
@Repository
public interface QuartzConfigRepository extends JpaRepository<QuartzConfig, Long> {

	/**
	 * 根据定时任务状态查询列表
	 * @param status 任务状态
	 * @return 定时任务表的List
	 */
	List<QuartzConfig> findByStatus(boolean status);

}
