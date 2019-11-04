package com.snh48.picq.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * @Description: 角色VO类
 *               <p>
 *               用于代替角色实体类接收参数。
 * @author JuFF_白羽
 * @date 2018年11月28日 上午11:40:26
 */
@Data
public class RoleVO implements Serializable {

	private static final long serialVersionUID = 3327849922539305196L;

	private Long id; // 编号

	private String role; // 角色标识程序中判断使用,如"admin",这个是唯一的:

	private String description; // 角色描述,UI界面显示使用

	private Boolean available = Boolean.FALSE; // 是否可用,如果不可用将不会添加给用户

}
