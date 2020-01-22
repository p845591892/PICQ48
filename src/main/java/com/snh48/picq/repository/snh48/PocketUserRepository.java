package com.snh48.picq.repository.snh48;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.snh48.picq.entity.snh48.PocketUser;

/**
 * 口袋48用户表DAO组件
 * 
 * @author shiro
 *
 */
@Repository
public interface PocketUserRepository extends JpaRepository<PocketUser, Long>, JpaSpecificationExecutor<PocketUser> {

}
