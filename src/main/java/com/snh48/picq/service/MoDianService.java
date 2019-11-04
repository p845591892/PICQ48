package com.snh48.picq.service;

/**
 * 摩点项目操作服务类
 * 
 * @author shiro
 *
 */
public interface MoDianService {

	/**
	 * @Description: 删除指定id的摩点项目，多个id可用“,”连接，并且会删除对应的集资信息。
	 * @author JuFF_白羽
	 * @param id ID集合
	 */
	public Integer deleteMoDianPoolProject(String id);

}
