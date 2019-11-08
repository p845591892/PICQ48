package com.snh48.picq.service;

import com.snh48.picq.entity.QQCommunity;

/**
 * @ClassName: QQCommunityService
 * @Description: QQ列表操作服务类
 * @author JuFF_白羽
 * @date 2018年8月10日 下午3:38:44
 */
public interface QQCommunityService {

	/**
	 * @Description: 新增一条QQ信息
	 * @author JuFF_白羽
	 */
	int addQQCommunity(QQCommunity qqCommunity);

	/**
	 * @Description: 修改一条QQ信息
	 * @author JuFF_白羽
	 */
	int updateQQCommunity(QQCommunity qqCommunity);

	/**
	 * @Description: 删除QQ信息以及关联信息
	 * @author JuFF_白羽
	 */
	int deleteQQCommunity(String id);
	
	/**
	 * 同步QQ列表
	 */
	boolean syncQQCommunity();

}
