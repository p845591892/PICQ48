package com.snh48.picq.entity.system;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * @Description: 权限表
 * @author JuFF_白羽
 * @date 2018年10月31日 下午4:30:04
 */
@Entity
@Table(name = "T_SYS_PERMISSION")
public class Permission implements Serializable{

	private static final long serialVersionUID = 6648039059437191128L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private Long id;// 主键.

	@Column(name = "NAME", length = 100)
	private String name;// 名称.

	@Column(name = "RESOURCE_TYPE", columnDefinition = "enum('menu','button')")
	private String resourceType;// 资源类型，[menu|button]

	@Column(name = "URL", length = 500)
	private String url;// 资源路径.

	@Column(name = "PERMISSION", length = 100)
	private String permission; // 权限字符串,menu例子：role:*，button例子：role:create,role:update,role:delete,role:view

	@Column(name = "PARENT_ID")
	private Long parentId; // 父编号

	@Column(name = "PARENT_IDS", length = 50)
	private String parentIds; // 父编号列表

	private Boolean available = Boolean.FALSE;

	@ManyToMany
	@JoinTable(name = "t_sys_role_permission", joinColumns = {
			@JoinColumn(name = "permission_id") }, inverseJoinColumns = { @JoinColumn(name = "role_id") })
	private List<Role> roles;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	public Boolean getAvailable() {
		return available;
	}

	public void setAvailable(Boolean available) {
		this.available = available;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	
}
