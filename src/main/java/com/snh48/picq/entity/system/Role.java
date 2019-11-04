package com.snh48.picq.entity.system;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * @Description: 角色表
 * @author JuFF_白羽
 * @date 2018年10月31日 下午4:25:51
 */
@Entity
@Table(name = "T_SYS_ROLE")
public class Role implements Serializable {

	private static final long serialVersionUID = -8006432066900692313L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private Long id; // 编号

	@Column(name = "ROLE", unique = true, length = 50)
	private String role; // 角色标识程序中判断使用,如"admin",这个是唯一的:

	@Column(name = "DESCRIPTION", length = 100)
	private String description; // 角色描述,UI界面显示使用

	private Boolean available = Boolean.FALSE; // 是否可用,如果不可用将不会添加给用户

	// 角色 -- 权限关系：多对多关系;
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "t_sys_role_permission", joinColumns = {
			@JoinColumn(name = "role_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "permission_id") })
	private List<Permission> permissions;

	// 用户 - 角色关系定义;
	@ManyToMany
	@JoinTable(name = "t_sys_user_role", joinColumns = { @JoinColumn(name = "role_id") }, inverseJoinColumns = {
			@JoinColumn(name = "user_id") })
	private List<User> users;// 一个角色对应多个用户

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getAvailable() {
		return available;
	}

	public void setAvailable(Boolean available) {
		this.available = available;
	}

	public List<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

}
