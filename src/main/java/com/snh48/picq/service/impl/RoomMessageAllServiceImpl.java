package com.snh48.picq.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snh48.picq.entity.snh48.RoomMessageAll;
import com.snh48.picq.repository.snh48.RoomMessageAllRepository;
import com.snh48.picq.service.RoomMessageAllService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@Transactional
public class RoomMessageAllServiceImpl implements RoomMessageAllService {

	@Autowired
	private RoomMessageAllRepository repository;

	@Override
	public void insert(RoomMessageAll roomMessageAll) {
		try {
			repository.save(roomMessageAll);
		} catch (Exception e) {
			log.error("保存口袋房间留言板消息失败。异常：{}", e.getMessage());
			throw e;
		}
	}

	@Override
	public void insert(List<RoomMessageAll> messages) {
		try {
			repository.saveAll(messages);
		} catch (Exception e) {
			log.error("保存口袋房间留言板消息集合失败。异常：{}", e.getMessage());
			throw e;
		}

	}

	@Override
	public Date getLastMessageDate(Long roomId) {
		// TODO Auto-generated method stub
		return null;
	}

}
