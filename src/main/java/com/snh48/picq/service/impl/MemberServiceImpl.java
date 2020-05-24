package com.snh48.picq.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snh48.picq.core.Common;
import com.snh48.picq.entity.snh48.Member;
import com.snh48.picq.exception.RepositoryException;
import com.snh48.picq.repository.snh48.MemberRepository;
import com.snh48.picq.service.MemberService;
import com.snh48.picq.utils.RedisUtil;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@Transactional
public class MemberServiceImpl implements MemberService {

	@Autowired
	private MemberRepository memberRepository;

	@Override
	public Member getCacheByRoomId(Long roomId) {
		String keyStr = Common.ROOM_MEMBER + String.valueOf(roomId);
		if (RedisUtil.exists(keyStr)) {// 如果缓存中存在，直接返回
			return (Member) RedisUtil.get(keyStr);
		} else {// 缓存中不存在，查询并写入缓存后返回
			Member member = memberRepository.findByRoomId(roomId);
			RedisUtil.setex(keyStr, member, Common.EXPIRE_TIME_SECOND_MEMBER);
			return member;
		}
	}

	@Override
	public List<Member> getMemberList(int monitorType) {
		List<Member> members = null;
		try {
			members = memberRepository.findByRoomMonitor(monitorType);
			if (null == members) {
				members = new ArrayList<Member>();
				log.info("method memberRepository.findByRoomMonitor(monitorType) result is null. monitorType={}",
						monitorType);
			}
		} catch (Exception e) {
			throw new RepositoryException("[Error] Get member list by roomMonitor exception:", e);
		}
		return members;
	}

}
