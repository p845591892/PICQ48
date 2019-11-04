package com.snh48.picq.repository.snh48;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.snh48.picq.entity.snh48.RoomMessage;

/**
 * @ClassName: RoomMessageRepository
 * @Description: 口袋房间消息表DAO组件
 * @author JuFF_白羽
 * @date 2018年7月11日 下午6:44:50
 */
@Repository
public interface RoomMessageRepository
		extends JpaRepository<RoomMessage, String>, JpaSpecificationExecutor<RoomMessage> {

	/**
	 * @Title: findByRoomIdAndIsSendOrderByMsgTimeAsc
	 * @Description: 根据房间ID获取未执行过发送任务的，按消息时间升序排列的口袋房间消息集合
	 * @author JuFF_白羽
	 * @param roomId 所属房间ID
	 * @param isSend 是否发送：1未发送，2已发送
	 * @return List<RoomMessage> 口袋房间消息集合
	 */
	List<RoomMessage> findByRoomIdAndIsSendOrderByMsgTimeAsc(Long roomId, int isSend);

	/**
	 * @Description: 获取指定日期后的口袋房间消息同步总量
	 * @param date 指定时间
	 * @date: 2018年9月24日 上午1:43:36
	 */
	@Query("select count(t) from RoomMessage t where t.msgTime >= ?1")
	Integer countGreaterDate(Date date);

	/**
	 * 获取指定口袋48房间的最新一条数据。
	 * 
	 * @return 消息实体对象
	 */
	RoomMessage findFirstByRoomIdOrderByMsgTimeDesc(Long roomId);

}
