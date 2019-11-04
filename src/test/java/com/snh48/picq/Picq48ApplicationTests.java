package com.snh48.picq;

import java.text.ParseException;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.snh48.picq.entity.snh48.Member;
import com.snh48.picq.entity.snh48.RoomMessage;
import com.snh48.picq.https.Pocket48Tool;
import com.snh48.picq.repository.QuartzConfigRepository;
import com.snh48.picq.repository.snh48.MemberRepository;
import com.snh48.picq.repository.snh48.RoomMessageRepository;
import com.snh48.picq.utils.DateUtil;

import redis.clients.jedis.Jedis;

@SpringBootTest
class Picq48ApplicationTests {

	@Autowired
	private Jedis jedis;

	@Test
	void jedisTest() {
		String key = "pocket_token";
		System.out.println(jedis.exists(key));
		System.out.println(jedis.setex(key, 10, "1234567890987654321"));
		System.out.println(jedis.exists(key));
	}

	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private QuartzConfigRepository quartzConfigRepository;
	@Autowired
	private RoomMessageRepository roomMessageRepository;

	@Test
	void repositoryTest() {
		RoomMessage message = roomMessageRepository.findFirstByRoomIdOrderByMsgTimeDesc(67265514l);
		System.out.println(DateUtil.getDate(message.getMsgTime()));
		System.out.println(message.getMsgTime().getTime());
		try {
			System.out.println(DateUtil.getDate(DateUtil.getDateFormat(message.getMsgTime().getTime())));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	void httpsTest() {
		List<Member> memberList = Pocket48Tool.getMemberList();
		for (Member member : memberList) {
			System.out.println(member.getName());
		}
	}

}
