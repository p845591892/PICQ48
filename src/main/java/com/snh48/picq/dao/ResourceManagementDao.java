package com.snh48.picq.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.snh48.picq.vo.RoomMessageVO;

/**
 * @Description: 资源管理服务DAO组件
 * @author JuFF_白羽
 * @date 2019年4月10日 下午3:38:57
 */
@Mapper
public interface ResourceManagementDao {

	/**
	 * @Description: 获取口袋房间消息列表
	 * @author JuFF_白羽
	 * @return List<RoomMessage> 口袋房间消息列表
	 */
	List<RoomMessageVO> findRoomMessage();

}
