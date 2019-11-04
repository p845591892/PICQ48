package com.snh48.picq.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.snh48.picq.entity.system.Permission;
import com.snh48.picq.entity.system.Role;
import com.snh48.picq.entity.system.User;

/**
 * @Description: 系统配置的DAO组件
 * @author JuFF_白羽
 * @date 2018年11月27日 下午4:05:01
 */
@Mapper
public interface SystemManageDao {

	/**
	 * @Description: 查询所有用户信息，获取用户列表
	 * @author JuFF_白羽
	 * @return List<User> 用户列表
	 */
	List<User> findUser();

	/**
	 * @Description: 根据用户ID修改用户信息
	 * @author JuFF_白羽
	 * @param
	 * @return int 受影响的行数
	 */
	int updateUserById(Map<String, Object> param);

	/**
	 * @Description: 查询所有角色信息，获取角色列表
	 * @author JuFF_白羽
	 * @return List<Role> 角色列表
	 */
	List<Role> findRole();

	/**
	 * @Description: 根据用户ID查询角色和用户角色关联表，获取非该用户的角色列表
	 * @author JuFF_白羽
	 * @param uid 用户ID
	 * @return List<Role> 角色列表
	 */
	List<Role> findHaventRoleByUid(Long uid);

	/**
	 * @Description: 根据用户ID查询角色和用户角色关联表，获取该用户的角色列表
	 * @author JuFF_白羽
	 * @param uid 用户ID
	 * @return List<Role> 角色列表
	 */
	List<Role> findHaveRoleByUid(Long uid);

	/**
	 * @Description: 新增用户-角色关联表
	 * @author JuFF_白羽
	 * @param uid  用户ID
	 * @param rids 角色ID数组
	 * @return int 受影响的行数
	 */
	int insertUserRole(Map<String, Object> param);

	/**
	 * @Description: 删除用户-角色关联表
	 * @author JuFF_白羽
	 * @param uid  用户ID
	 * @param rids 角色ID数组
	 * @return int 受影响的行数
	 */
	int deleteUserRole(Map<String, Object> param);

	/**
	 * @Description: 修改角色信息
	 * @author JuFF_白羽
	 * @param param 角色表字段的参数集合
	 * @return int 受影响的行数
	 */
	int updateRole(Map<String, Object> param);

	/**
	 * @Description: 根据角色ID查询权限表和角色权限关联表，获取角色未被赋权的URL和BUTTON
	 * @author JuFF_白羽
	 * @param rid 角色ID
	 * @return List<Permission> 权限列表
	 */
	List<Permission> findHaventPermissionsByRid(Long rid);

	/**
	 * @Description: 根据角色ID查询权限表和角色权限关联表，获取角色未被赋权的URL和BUTTON
	 * @author JuFF_白羽
	 * @param rid 角色ID
	 * @return List<Permission> 权限列表
	 */
	List<Permission> findHavePermissionsByRid(Long rid);

	/**
	 * @Description: 新增角色-资源关联表
	 * @author JuFF_白羽
	 * @param rid  角色ID
	 * @param pids 资源ID集合
	 * @return int 受影响的行数
	 */
	int insertRolePermission(Map<String, Object> param);

	/**
	 * @Description: 删除角色-资源关联表
	 * @author JuFF_白羽
	 * @param rid  角色ID
	 * @param pids 资源ID集合
	 * @return int 受影响的行数
	 */
	int deleteRolePermission(Map<String, Object> param);

	/**
	 * @Description: 根据资源ID查询资源表，获取ID和且父ID都符合的资源列表
	 * @author JuFF_白羽
	 * @param pid 资源ID
	 * @return List<Permission> 资源列表
	 */
	List<Permission> findPermission(Map<String, Object> param);

	/**
	 * @Description: 根据资源ID修改一条资源信息
	 * @author JuFF_白羽
	 * @param param 资源表字段参数集合
	 * @return int 受影响的行数
	 */
	int updatePermission(Map<String, Object> param);

}
