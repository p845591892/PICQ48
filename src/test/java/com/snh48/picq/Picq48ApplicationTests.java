//package com.snh48.picq;
//
//import java.util.List;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import com.snh48.picq.entity.snh48.Member;
//import com.snh48.picq.entity.snh48.RoomMessage;
//import com.snh48.picq.https.Pocket48Tool;
//import com.snh48.picq.repository.QQCommunityRepository;
//import com.snh48.picq.repository.QuartzConfigRepository;
//import com.snh48.picq.repository.snh48.MemberRepository;
//import com.snh48.picq.repository.snh48.RoomMessageRepository;
//import com.snh48.picq.service.ResourceManagementService;
//
//import cc.moecraft.icq.PicqBotX;
//import cc.moecraft.icq.sender.IcqHttpApi;
//import cc.moecraft.icq.sender.message.MessageBuilder;
//import cc.moecraft.icq.sender.message.components.ComponentRecord;
//import redis.clients.jedis.Jedis;
//
//@SpringBootTest
//class Picq48ApplicationTests {
//
//	@Autowired
//	private Jedis jedis;
//
//	@Test
//	void jedisTest() {
//		String key = "pocket_token";
//		System.out.println(jedis.exists(key));
//		System.out.println(jedis.setex(key, 10, "1234567890987654321"));
//		System.out.println(jedis.exists(key));
//	}
//
//	@Autowired
//	private MemberRepository memberRepository;
//	@Autowired
//	private QuartzConfigRepository quartzConfigRepository;
//	@Autowired
//	private RoomMessageRepository roomMessageRepository;
//	@Autowired
//	private QQCommunityRepository qqCommunityRepository;
//	@Autowired
//	private ResourceManagementService resourceManagementService;
//
//	@Test
//	void repositoryTest() {
//		List<Member> memberList = memberRepository.findByRoomMonitor(1);
//		for (Member member : memberList) {
//			RoomMessage message = roomMessageRepository.findFirstByRoomIdOrderByMsgTimeDesc(member.getRoomId());
//			System.out.println(message.toString());
//			System.out.println(message.getId());
//		}
//	}
//
//	@Test
//	void httpsTest() {
//		List<Member> memberList = Pocket48Tool.getMemberList();
//		for (Member member : memberList) {
//			System.out.println(member.getName());
//		}
//	}
//
//}