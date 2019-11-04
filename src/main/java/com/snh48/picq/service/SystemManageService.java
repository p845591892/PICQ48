package com.snh48.picq.service;

import java.util.List;

import com.snh48.picq.entity.system.Permission;
import com.snh48.picq.entity.system.Role;
import com.snh48.picq.entity.system.User;
import com.snh48.picq.vo.PermissionVO;
import com.snh48.picq.vo.RoleVO;
import com.snh48.picq.vo.UserVO;

/**
 * @Description: 系统配置的服务类
 *               <p>
 *               提供系统配置相关的服务。
 * @author JuFF_白羽
 * @date 2018年11月27日 下午3:35:06
 */
public interface SystemManageService {

	/**
	 * 获取系统用户列表
	 */
	public List<User> getUsers();

	/**
	 * 修改用户信息
	 * 
	 * @param user
	 * @return
	 */
	public int updateUser(UserVO user);

	/**
	 * 获取系统角色列表
	 * 
	 * @return
	 */
	public List<Role> getRoles();

	/**
	 * 新增角色
	 * 
	 * @param role
	 * @return
	 */
	public int addRole(RoleVO role);

	/**
	 * @Description: 根据用户ID获取用户未拥有的角色列表
	 * @author JuFF_白羽
	 * @param uid 用户ID
	 */
	public List<Role> getHaventRolesByUid(Long uid);

	/**
	 * @Description: 根据用户ID获取用户拥有的角色列表
	 * @author JuFF_白羽
	 * @param uid 用户ID
	 */
	public List<Role> getHaveRolesByUid(Long uid);

	/**
	 * @Description: 根据用户ID，为用户赋予新角色
	 * @author JuFF_白羽
	 * @param uid  用户ID
	 * @param rids 角色ID数组
	 */
	public int addUserRole(Long uid, String rids);

	/**
	 * @Description: 根据用户ID，为用户撤销已赋予的角色
	 * @author JuFF_白羽
	 * @param uid  用户ID
	 * @param rids 角色ID数组
	 */
	public int deleteUserRole(Long uid, String rids);

	/**
	 * @Description: 根据角色ID，修改角色信息
	 * @author JuFF_白羽
	 * @param role 角色实体类
	 */
	public int updateRole(RoleVO role);

	/**
	 * @Description: 根据角色ID删除一条角色
	 * @author JuFF_白羽
	 * @param id 角色ID
	 */
	public int deleteRole(Long id);

	/**
	 * @Description: 根据角色ID，获取角色未被赋权的URL或BUTTON
	 * @author JuFF_白羽
	 * @param rid 角色ID
	 * @return List<Permission> 资源列表
	 */
	public List<Permission> getHaventPermissionsByRid(Long rid);

	/**
	 * @Description: 根据角色ID，获取角色已获赋权的URL或BUTTON
	 * @author JuFF_白羽
	 * @param rid 角色ID
	 * @return List<Permission> 资源列表
	 */
	public List<Permission> getHavePermissionsByRid(Long rid);

	/**
	 * @Description: 根据角色ID，为角色赋予新的资源权限
	 * @author JuFF_白羽
	 * @param rid  角色ID
	 * @param pids 资源ID集合
	 * @return int 新增数据条数
	 */
	public int addRolePermission(Long rid, String pids);

	/**
	 * @Description: 根据角色ID，撤销角色已获权限的资源
	 * @author JuFF_白羽
	 * @param rid  角色ID
	 * @param pids 资源ID集合
	 * @return int 删除数据条数
	 */
	public int deleteRolePermission(Long rid, String pids);

	/**
	 * @Description: 获取权限资源列表
	 * @author JuFF_白羽
	 * @return List<Permission> 权限资源列表
	 */
	public List<Permission> getSystemPermission(Long id, Long pid);

	/**
	 * @Description: 新增资源
	 * @author JuFF_白羽
	 * @param permission 资源对象
	 */
	public int addPermission(PermissionVO permission);

	/**
	 * @Description: 修改一条资源信息
	 * @author JuFF_白羽
	 * @param permission 资源对象
	 */
	public int updatePermission(PermissionVO permission);

	/**
	 * @Description: 删除一条资源
	 * @author JuFF_白羽
	 * @param id 资源ID
	 */
	public int deletePermission(Long id);

}
