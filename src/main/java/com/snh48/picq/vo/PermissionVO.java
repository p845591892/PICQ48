package com.snh48.picq.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * @Description: 资源VO类
 *               <P>
 *               用于代替资源实体类接收参数。
 * @author JuFF_白羽
 * @date 2018年12月6日 上午11:22:41
 */
@Data
public class PermissionVO implements Serializable {

	private static final long serialVersionUID = -7678355125666951905L;

	private Long id;// 主键.

	private String name;// 名称.

	private String resourceType;// 资源类型，[menu|button]

	private String url;// 资源路径.

	private String permission; // 权限字符串,menu例子：role:*，button例子：role:create,role:update,role:delete,role:view

	private Long parentId; // 父编号

	private String parentIds; // 父编号列表

	private Boolean available = Boolean.FALSE;

}
