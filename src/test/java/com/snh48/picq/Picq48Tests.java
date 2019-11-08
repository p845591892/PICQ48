//package com.snh48.picq;
//
//import java.util.Date;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.BeanUtils;
//
//import com.snh48.picq.entity.snh48.Member;
//import com.snh48.picq.https.WeiboTool;
//import com.snh48.picq.utils.DateUtil;
//
//public class Picq48Tests {
//
//	@Test
//	public void test1() {
//		Member m1 = new Member();
//		m1.setAbbr("111");
//		m1.setAvatar("222");
//		m1.setBirthday("333");
//
//		Member m2 = new Member();
//		m2.setBirthplace("444");
//		m2.setBloodType("555");
//		m2.setConstellation("666");
//
//		System.out.println("m1:");
//		System.out.println(m1.toString());
//		System.out.println("m2:");
//		System.out.println(m2.toString());
//
//		BeanUtils.copyProperties(m1, m2);
//
//		System.out.println("m1:");
//		System.out.println(m1.toString());
//		System.out.println("m2:");
//		System.out.println(m2.toString());
//	}
//
//	@Test
//	public void httpsTest() {
//		WeiboTool.getDynamicList(1076036691967435l).forEach(dynamic -> {
//			System.out.println(dynamic.toString());
//		});
//	}
//
//	@Test
//	public void test2() {
//		long a = new Date().getTime();
//		long b = DateUtil.countDayToDate(-1).getTime();
//
//		System.out.println("a = " + a);
//		System.out.println("b = " + b);
//		System.out.println(b >= a);
//	}
//
//}
