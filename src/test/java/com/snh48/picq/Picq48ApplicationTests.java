//package com.snh48.picq;
//
//import java.io.IOException;
//import java.security.KeyManagementException;
//import java.security.NoSuchAlgorithmException;
//import java.text.ParseException;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.configurationprocessor.json.JSONException;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.Page;
//
//import com.snh48.picq.entity.snh48.Member;
//import com.snh48.picq.entity.snh48.PocketUser;
//import com.snh48.picq.entity.snh48.RoomMessage;
//import com.snh48.picq.entity.snh48.RoomMessageAll;
//import com.snh48.picq.https.HttpsPICQ48;
//import com.snh48.picq.https.Pocket48Tool;
//import com.snh48.picq.repository.snh48.MemberRepository;
//import com.snh48.picq.repository.snh48.PocketUserRepository;
//import com.snh48.picq.repository.snh48.RoomMessageAllRepository;
//import com.snh48.picq.repository.snh48.RoomMessageRepository;
//import com.snh48.picq.service.ResourceManagementService;
//import com.snh48.picq.utils.DateUtil;
//import com.snh48.picq.utils.StringUtil;
//import com.snh48.picq.vo.MemberVO;
//
//@SpringBootTest
//class Picq48ApplicationTests {
//
//	@Autowired
//	private MemberRepository memberRepository;
//	@Autowired
//	private RoomMessageRepository roomMessageRepository;
//
//	@Test
//	public void repositoryTest() {
//		List<Member> memberList = memberRepository.findByRoomMonitor(1);
//		for (Member member : memberList) {
//			RoomMessage message = roomMessageRepository.findFirstByRoomIdOrderByMsgTimeDesc(member.getRoomId());
//			System.out.println(message.toString());
//			System.out.println(message.getId());
//		}
//	}
//
//	/**
//	 * 抓取房间今年的口袋消息。
//	 * 
//	 * @throws ParseException
//	 * @throws InterruptedException
//	 */
//	@Test
//	public void syncRoomMessageByYear() throws ParseException, InterruptedException {
//		/* 2019-01-01 00:00:00 的时间戳 */
//		long overTime = DateUtil.getDateFormat("2019-01-0100:00:00").getTime();
//		/* 郭爽 */
////		String memberId = "867892";
////		String roomId = "67275562";
//		/* 杨媛媛 */
////		String memberId = "417331";
////		String roomId = "67342057";
//		/* 徐楚雯 */
////		String memberId = "707371";
////		String roomId = "67265540";
//		/* 金莹钥 */
////		String memberId = "538735";
////		String roomId = "67370575";
//		/* 罗可嘉 */
//		String memberId = "607521";
//		String roomId = "67370588";
//		/* 卢静 */
////		String memberId = "327569";
////		String roomId = "67217596";
//		/* 陈桂君 */
////		String memberId = "417311";
////		String roomId = "67370540";
//		/* 郭爽 */
////		String memberId = "867892";
////		String roomId = "67275562";
//
//		// 循环体
////		long nextTime = 0;
////		long nextTime = DateUtil.getDateFormat("2020-01-0100:00:00").getTime();
//		long nextTime = DateUtil.getDateFormat("2019-11-0419:43:08").getTime();
//		do {
//			// 每次循环暂停10秒
//			Thread.sleep(10000);
//			// 获取消息集合，并获取最后一条消息的时间戳赋值给nextTime
//			List<RoomMessage> roomMessageList = Pocket48Tool.getRoomMessageList(memberId, roomId, nextTime);
//			int lastIndex = roomMessageList.size() - 1;
//			nextTime = roomMessageList.get(lastIndex).getMsgTime().getTime();
//			// 写入数据库
//			for (RoomMessage roomMessage : roomMessageList) {
//				roomMessageRepository.save(roomMessage);
//			}
//
//			System.out.println("------------------当前时间 " + DateUtil.getDate(nextTime));
//		} while (nextTime >= overTime);
//		// 如果最后一条消息的时间戳小于等于overTime则结束循环，
//		// 否则nextTime将作为下次循环的请求参数
//
//	}
//
//	@Autowired
//	private RoomMessageAllRepository roomMessageAllRepository;
//
//	/**
//	 * 同步留言板消息
//	 * 
//	 * @throws ParseException
//	 * @throws InterruptedException
//	 */
//	@Test
//	public void syncRoomMessageAllByYear() throws ParseException, InterruptedException {
//
//		long overTime = DateUtil.getDateFormat("2019-01-0100:00:00").getTime();
//		long nextTime = DateUtil.getDateFormat("2020-05-1900:11:09").getTime();
////		long nextTime = DateUtil.getDateFormat("2019-07-3100:27:28").getTime();
//		/* 杨媛媛 */
////		long roomId = 67342057;
//		/* 徐楚雯 */
////		 long roomId = 67265540l;
//		/* 金莹钥 */
////		long roomId = 67370575l;
//		/* 罗可嘉 */
////		long roomId = 67370588l;
//		/* 卢静 */
////		long roomId = 67217596l;
//		/* 陈桂君 */
////		long roomId = 67370540l;
//		/* 郭爽 */
//		long roomId = 67275562l;
//		/* 李慧 */
////		long roomId = 67207862l;
//
//		do {
//			// 每次循环暂停8秒
//			Thread.sleep(5000);
//			List<RoomMessageAll> list = list = Pocket48Tool.getRoomMessageAllList(nextTime, false, roomId);
//			;
//			try {
//				int lastIndex = list.size() - 1;
//				nextTime = list.get(lastIndex).getMessageTime().getTime();
//			} catch (NullPointerException e) {
//				System.out.println("------------------当前时间 " + DateUtil.getDate(nextTime));
//				continue;
//			}
//			// 持久化
//			roomMessageAllRepository.saveAll(list);
//
//			System.out.println("------------------当前时间 " + DateUtil.getDate(nextTime));
//		} while (nextTime >= overTime);
//
//	}
//
//	@Autowired
//	private PocketUserRepository pocketUserRepository;
//
//	/**
//	 * 同步留言板中发言的用户信息
//	 * 
//	 * @throws InterruptedException
//	 */
//	@Test
//	public void syncPocketUser() throws InterruptedException {
//		long version = 20200120l;
//		List<Long> userIds = roomMessageAllRepository.getSenderUserIdListByVersion(version);
//		System.out.println("-------------------一共要查询 " + userIds.size() + " 个用户。");
//		for (Long userId : userIds) {
//			Thread.sleep(5000);
//			PocketUser user = Pocket48Tool.getPocketUser(0, userId);
//			try {
//				user.setVersion(version);
//			} catch (NullPointerException e) {
//				continue;
//			}
//			pocketUserRepository.save(user);
//		}
//	}
//
//	@Test
//	void pocket48ToolTest() {
//		try {
//			System.out.println(HttpsPICQ48.httpsRoomMessage("19", "67333101", 0));
//		} catch (KeyManagementException | NoSuchAlgorithmException | IOException | JSONException e) {
//			e.printStackTrace();
//		}
//	}
//
//	@Autowired
//	private ResourceManagementService resourceManagementService;
//
//	@Test
//	void lambdaTest() {
//		String arg = "zy";
//		MemberVO vo = new MemberVO();
//		vo.setAbbr(arg);
//		// 查询
//		Page<Member> memberPage = resourceManagementService.getMembers(1, 20, vo);
//		List<Member> responListP = new ArrayList<Member>();// 精
//		memberPage.stream().filter((p) -> p.getName().equals(arg) || p.getAbbr().equals(arg))
//				.forEach((p) -> responListP.add(p));
//		responListP.forEach((p) -> System.out.println(p.toString()));
//	}
//
//}