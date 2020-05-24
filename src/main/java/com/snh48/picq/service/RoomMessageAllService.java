package com.snh48.picq.service;

import java.util.Date;
import java.util.List;

import com.snh48.picq.entity.snh48.RoomMessageAll;

/**
 * 口袋房间留言板服务接口
 * 
 * @author shiro
 *
 */
public interface RoomMessageAllService {

	void insert(RoomMessageAll p);

	void insert(List<RoomMessageAll> messages);

	Date getLastMessageDate(Long roomId);

}
