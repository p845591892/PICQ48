package com.snh48.picq.repository.system;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.snh48.picq.entity.system.SystemLog;

/**
 * 系统日志表仓库
 * 
 * @author shiro
 *
 */
@Repository
public interface SystemLogRepository extends JpaRepository<SystemLog, Long>, JpaSpecificationExecutor<SystemLog> {

}
