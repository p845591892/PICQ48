package com.snh48.picq.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snh48.picq.dao.SystemManageDao;
import com.snh48.picq.entity.system.Permission;
import com.snh48.picq.entity.system.Role;
import com.snh48.picq.entity.system.User;
import com.snh48.picq.repository.system.PermissionRepository;
import com.snh48.picq.repository.system.RoleRepository;
import com.snh48.picq.service.SystemManageService;
import com.snh48.picq.vo.PermissionVO;
import com.snh48.picq.vo.RoleVO;
import com.snh48.picq.vo.UserVO;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@Transactional
public class SystemManageServiceImpl implements SystemManageService {

	/**
	 * 系统配置的DAO组件
	 */
	@Autowired
	private SystemManageDao systemManageDao;

	/**
	 * 角色表DAO组件
	 */
	@Autowired
	private RoleRepository roleRepository;

	/**
	 * 权限资源表DAO组件
	 */
	@Autowired
	private PermissionRepository permissionRepository;

	public List<User> getUsers() {
		return systemManageDao.findUser();
	}

	public int updateUser(UserVO user) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id", user.getId());
		param.put("nickname", user.getNickname());
		param.put("state", user.getState());
		try {
			return systemManageDao.updateUserById(param);
		} catch (Exception e) {
			log.info(e.toString());
			return 0;
		}
	}

	public List<Role> getRoles() {
		return systemManageDao.findRole();
	}

	public int addRole(RoleVO role) {
		Role param = new Role();
		param.setAvailable(true);
		param.setDescription(role.getDescription());
		param.setRole(role.getRole());
		try {
			roleRepository.save(param);
			return 1;
		} catch (Exception e) {
			log.info(e.toString());
			return 0;
		}
	}

	public List<Role> getHaventRolesByUid(Long uid) {
		return systemManageDao.findHaventRoleByUid(uid);
	}

	public List<Role> getHaveRolesByUid(Long uid) {
		return systemManageDao.findHaveRoleByUid(uid);
	}

	public int addUserRole(Long uid, String rids) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("uid", uid);
		param.put("rids", rids.split(","));
		return systemManageDao.insertUserRole(param);
	}

	public int deleteUserRole(Long uid, String rids) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("uid", uid);
		param.put("rids", rids.split(","));
		return systemManageDao.deleteUserRole(param);
	}

	public int updateRole(RoleVO role) {
		if (role.getId() == null) {
			return 0;
		}
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id", role.getId());
		param.put("role", role.getRole());
		param.put("description", role.getDescription());
		param.put("available", role.getAvailable());
		return systemManageDao.updateRole(param);
	}

	public int deleteRole(Long id) {
		try {
			roleRepository.deleteById(id);
			return 1;
		} catch (Exception e) {
			log.info(e.toString());
			return 0;
		}
	}

	public List<Permission> getHaventPermissionsByRid(Long rid) {
		return systemManageDao.findHaventPermissionsByRid(rid);
	}

	public List<Permission> getHavePermissionsByRid(Long rid) {
		return systemManageDao.findHavePermissionsByRid(rid);
	}

	public int addRolePermission(Long rid, String pids) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("rid", rid);
		param.put("pids", pids.split(","));
		try {
			return systemManageDao.insertRolePermission(param);
		} catch (Exception e) {
			log.info(e.toString());
			return 0;
		}
	}

	public int deleteRolePermission(Long rid, String pids) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("rid", rid);
		param.put("pids", pids.split(","));
		try {
			return systemManageDao.deleteRolePermission(param);
		} catch (Exception e) {
			log.info(e.toString());
			return 0;
		}
	}

	public List<Permission> getSystemPermission(Long id, Long pid) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id", id);
		param.put("pid", pid);
		return systemManageDao.findPermission(param);
	}

	public int addPermission(PermissionVO vo) {
		Permission permission = new Permission();
		permission.setName(vo.getName());
		permission.setParentId(vo.getParentId());
		permission.setPermission(vo.getPermission());
		permission.setResourceType(vo.getResourceType());
		permission.setUrl(vo.getUrl());
		try {
			permissionRepository.save(permission);
			return 1;
		} catch (Exception e) {
			log.info(e.toString());
			return 0;
		}
	}

	public int updatePermission(PermissionVO permission) {
		if (permission.getId() == null) {
			return 0;
		}
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id", permission.getId());
		param.put("name", permission.getName());
		param.put("resourceType", permission.getResourceType());
		param.put("url", permission.getUrl());
		param.put("permission", permission.getPermission());
		param.put("parentId", permission.getParentId());
		return systemManageDao.updatePermission(param);
	}

	public int deletePermission(Long id) {
		try {
			permissionRepository.deleteById(id);
			return 1;
		} catch (Exception e) {
			log.info(e.toString());
			return 0;
		}
	}

}
