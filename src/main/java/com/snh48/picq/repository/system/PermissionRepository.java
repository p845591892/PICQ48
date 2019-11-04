package com.snh48.picq.repository.system;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.snh48.picq.entity.system.Permission;

/**
 * @Description: 权限资源表DAO组件
 * @author JuFF_白羽
 * @date 2018年12月5日 上午11:56:13
 */
@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

	/**
	 * @Description: 查询资源表，根据资源ID或父ID获取符合条件的资源列表
	 * @author JuFF_白羽
	 * @param id  资源ID
	 * @param pid 资源的父ID
	 * @return List<Permission> 资源列表
	 */
	List<Permission> findByIdOrParentId(Long id, Long parentId);

}
