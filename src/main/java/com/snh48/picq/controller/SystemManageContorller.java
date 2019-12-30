package com.snh48.picq.controller;

import javax.net.ssl.HttpsURLConnection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.snh48.picq.annotation.Log;
import com.snh48.picq.annotation.OperationType;
import com.snh48.picq.service.SystemManageService;
import com.snh48.picq.vo.PermissionVO;
import com.snh48.picq.vo.ResultVO;
import com.snh48.picq.vo.RoleVO;
import com.snh48.picq.vo.UserVO;

/**
 * @Description: 系统配置模块数据操作接口
 * @author JuFF_白羽
 * @date 2018年11月27日 下午5:11:09
 */
@RestController
public class SystemManageContorller {

	@Autowired
	private SystemManageService systemManageService;

	/**
	 * @Description: 修改用户信息
	 * @author JuFF_白羽
	 */
	@Log(desc = "修改用户信息", type = OperationType.UPDATE)
	@PostMapping("/system-user/update")
	public ResultVO updateUser(UserVO user) {
		ResultVO result = new ResultVO();
		int i = systemManageService.updateUser(user);
		if (i == 1) {
			result.setStatus(HttpsURLConnection.HTTP_OK);
		} else {
			result.setStatus(400);
			result.setCause("修改失败");
		}
		return result;
	}

	/**
	 * @Description: 为用户赋予新角色
	 * @author JuFF_白羽
	 */
	@Log(desc = "为用户赋予角色", type = OperationType.ADD)
	@PostMapping("/system-user/role/add")
	public ResultVO addUserRole(Long uid, String rids) {
		ResultVO result = new ResultVO();
		if (uid == null || rids == null) {
			result.setStatus(400);
			result.setCause("用户ID或者角色ID数组不能为空");
		} else {
			int i = systemManageService.addUserRole(uid, rids);
			if (i >= 1) {
				result.setStatus(HttpsURLConnection.HTTP_OK);
			} else {
				result.setStatus(400);
				result.setCause("设置失败");
			}
		}
		return result;
	}

	/**
	 * @Description: 为用户撤销已赋予的角色
	 * @author JuFF_白羽
	 */
	@Log(desc = "撤销用户已拥有的角色", type = OperationType.DEL)
	@PostMapping("/system-user/role/delete")
	public ResultVO deleteUserRole(Long uid, String rids) {
		ResultVO result = new ResultVO();
		if (uid == null || rids == null) {
			result.setStatus(400);
			result.setCause("用户ID或者角色ID数组不能为空");
		} else {
			int i = systemManageService.deleteUserRole(uid, rids);
			if (i >= 1) {
				result.setStatus(HttpsURLConnection.HTTP_OK);
			} else {
				result.setStatus(400);
				result.setCause("设置失败");
			}
		}
		return result;
	}

	/**
	 * @Description: 新增角色
	 * @author JuFF_白羽
	 */
	@Log(desc = "新增角色", type = OperationType.ADD)
	@PostMapping("/system-role/add")
	public ResultVO addRole(RoleVO role) {
		ResultVO result = new ResultVO();
		int i = systemManageService.addRole(role);
		if (i == 1) {
			result.setStatus(HttpsURLConnection.HTTP_OK);
		} else {
			result.setStatus(400);
			result.setCause("新增失败");
		}
		return result;
	}

	/**
	 * @Description: 修改角色信息
	 * @author JuFF_白羽
	 */
	@Log(desc = "修改角色信息", type = OperationType.UPDATE)
	@PostMapping("/system-role/update")
	public ResultVO updateRole(RoleVO role) {
		ResultVO result = new ResultVO();
		int i = systemManageService.updateRole(role);
		if (i == 1) {
			result.setStatus(HttpsURLConnection.HTTP_OK);
		} else if (i == 0) {
			result.setStatus(400);
			result.setCause("ID不能为空");
		} else {
			result.setStatus(400);
			result.setCause("修改失败");
		}
		return result;
	}

	/**
	 * @Description: 删除角色
	 * @author JuFF_白羽
	 */
	@Log(desc = "删除角色信息", type = OperationType.DEL)
	@PostMapping("/system-role/delete")
	public ResultVO deleteRole(Long id) {
		ResultVO result = new ResultVO();
		int i = systemManageService.deleteRole(id);
		if (i == 1) {
			result.setStatus(HttpsURLConnection.HTTP_OK);
		} else {
			result.setStatus(400);
			result.setCause("删除失败");
		}
		return result;
	}

	/**
	 * @Description: 为角色赋予新的资源权限
	 * @author JuFF_白羽
	 */
	@Log(desc = "赋予角色新的资源权限", type = OperationType.ADD)
	@PostMapping("/system-role/permission/add")
	public ResultVO addRolePermission(Long rid, String pids) {
		ResultVO result = new ResultVO();
		if (rid == null || pids == null) {
			result.setStatus(400);
			result.setCause("角色ID和资源ID数组不能为空");
		} else {
			int i = systemManageService.addRolePermission(rid, pids);
			if (i >= 1) {
				result.setStatus(HttpsURLConnection.HTTP_OK);
			} else {
				result.setStatus(400);
				result.setCause("设置失败");
			}
		}
		return result;
	}

	/**
	 * @Description: 为角色撤销已赋予资源权限
	 * @author JuFF_白羽
	 */
	@Log(desc = "撤销角色已拥有的资源权限", type = OperationType.DEL)
	@PostMapping("/system-role/permission/delete")
	public ResultVO deleteRolePermission(Long rid, String pids) {
		ResultVO result = new ResultVO();
		if (rid == null || pids == null) {
			result.setStatus(400);
			result.setCause("角色ID和资源ID数组不能为空");
		} else {
			int i = systemManageService.deleteRolePermission(rid, pids);
			if (i >= 1) {
				result.setStatus(HttpsURLConnection.HTTP_OK);
			} else {
				result.setStatus(400);
				result.setCause("设置失败");
			}
		}
		return result;
	}

	/**
	 * @Description: 新增资源
	 * @author JuFF_白羽
	 */
	@Log(desc = "新增资源", type = OperationType.ADD)
	@PostMapping("/system-permission/add")
	public ResultVO addPermission(PermissionVO permission) {
		ResultVO result = new ResultVO();
		int i = systemManageService.addPermission(permission);
		if (i == 1) {
			result.setStatus(HttpsURLConnection.HTTP_OK);
		} else {
			result.setStatus(400);
			result.setCause("新增失败");
		}
		return result;
	}

	/**
	 * @Description: 修改资源信息
	 * @author JuFF_白羽
	 */
	@Log(desc = "修改资源信息", type = OperationType.UPDATE)
	@PostMapping("/system-permission/update")
	public ResultVO updatePermission(PermissionVO permission) {
		ResultVO result = new ResultVO();
		int i = systemManageService.updatePermission(permission);
		if (i == 1) {
			result.setStatus(HttpsURLConnection.HTTP_OK);
		} else if (i == 0) {
			result.setStatus(400);
			result.setCause("ID不能为空");
		} else {
			result.setStatus(400);
			result.setCause("新增失败");
		}
		return result;
	}

	/**
	 * @Description: 删除资源
	 * @author JuFF_白羽
	 */
	@Log(desc = "删除资源", type = OperationType.DEL)
	@PostMapping("/system-permission/delete")
	public ResultVO deletePermission(Long id) {
		ResultVO result = new ResultVO();
		int i = systemManageService.deletePermission(id);
		if (i == 1) {
			result.setStatus(HttpsURLConnection.HTTP_OK);
		} else {
			result.setStatus(400);
			result.setCause("删除失败");
		}
		return result;
	}

}
