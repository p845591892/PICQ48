package com.snh48.picq.repository.system;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.snh48.picq.entity.system.Role;

/**
 * @Description: 角色表DAO组件
 * @author JuFF_白羽
 * @date 2018年11月28日 下午5:01:20
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

}
