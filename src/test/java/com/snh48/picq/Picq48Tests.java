//package com.snh48.picq;
//
//import java.io.IOException;
//import java.security.KeyManagementException;
//import java.security.NoSuchAlgorithmException;
//import java.text.ParseException;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.Callable;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ThreadFactory;
//import java.util.concurrent.atomic.AtomicInteger;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.BeanUtils;
//import org.springframework.http.HttpMethod;
//
//import com.snh48.picq.entity.snh48.Member;
//import com.snh48.picq.https.HttpsURL;
//import com.snh48.picq.https.MyHttpHeaders;
//import com.snh48.picq.https.MyMediaType;
//import com.snh48.picq.utils.DateUtil;
//import com.snh48.picq.utils.Https;
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
//	/**
//	 * 获取token
//	 * 
//	 * @throws KeyManagementException
//	 * @throws NoSuchAlgorithmException
//	 * @throws IOException
//	 */
//	@Test
//	public void getToken() throws KeyManagementException, NoSuchAlgorithmException, IOException {
//		Https https = new Https();
//		/* 请求头 */
//		Map<String, String> requestPropertys = new HashMap<String, String>();
//		requestPropertys.put(MyHttpHeaders.ACCEPT, MyMediaType.ALL_VALUE);
//		requestPropertys.put(MyHttpHeaders.CONTENT_TYPE, MyMediaType.APPLICATION_JSON_UTF8_VALUE);
//		requestPropertys.put(MyHttpHeaders.USER_AGENT, MyMediaType.USER_AGENT_IPHONE);
//		requestPropertys.put(MyHttpHeaders.APPINFO, MyMediaType.APPINFO);
//		/* 请求参数 */
//		String payloadJson = "{\"mobile\":\"\",\"pwd\":\"\"}";
//		/* 发送请求 */
//		String loginJson = https.setUrl(HttpsURL.TOKEN).setDataType(HttpMethod.POST.name()).setPayloadJson(payloadJson)
//				.setRequestProperty(requestPropertys).send();
//		System.out.println(loginJson);
//	}
//
//	private String token = "RElms9gB3ZvrtE69+zZffUqG+dXnvGJSLf4qPA8PgW1F2O4tlNv9CP1oQbHqUK+1ZuDAkFmZTgYA8znPgGnZz5J8BOB67jFxeGXAMaXoHaHz+48Oo3NNwgXGUfkv5g8R";
//
//	/**
//	 * 获取用户信息
//	 * 
//	 * @throws KeyManagementException
//	 * @throws NoSuchAlgorithmException
//	 * @throws IOException
//	 */
//	@Test
//	public void getUser() throws KeyManagementException, NoSuchAlgorithmException, IOException {
//		Https https = new Https();
//		/* 请求头 */
//		Map<String, String> requestPropertys = new HashMap<String, String>();
//		requestPropertys.put(MyHttpHeaders.ACCEPT, MyMediaType.ALL_VALUE);
//		requestPropertys.put(MyHttpHeaders.CONTENT_TYPE, MyMediaType.APPLICATION_JSON_UTF8_VALUE);
//		requestPropertys.put(MyHttpHeaders.USER_AGENT, MyMediaType.USER_AGENT_IPHONE);
//		requestPropertys.put(MyHttpHeaders.APPINFO, MyMediaType.APPINFO);
//		requestPropertys.put(MyHttpHeaders.POCKET_TOKEN, token);
//		/* 请求参数 */
//		String payloadJson = "{\"needMuteInfo\":0,\"userId\":\"674325\"}";
//		/* 发送请求 */
//		String jsonStr = https.setDataType(HttpMethod.POST.name()).setRequestProperty(requestPropertys)
//				.setPayloadJson(payloadJson).setUrl(HttpsURL.USER_SMALL).send();
//		System.out.println(jsonStr);
//	}
//
//	@Test
//	public void https() throws ParseException {
//		long nextTime = DateUtil.getDateFormat("2020-01-0100:00:00").getTime();
//		boolean needTop1Msg = false;
//		long roomId = 67342057;
//
//		Https https = new Https();
//		/* 请求头 */
//		Map<String, String> requestPropertys = new HashMap<String, String>();
//		requestPropertys.put(MyHttpHeaders.ACCEPT, MyMediaType.ALL_VALUE);
//		requestPropertys.put(MyHttpHeaders.CONTENT_TYPE, MyMediaType.APPLICATION_JSON_UTF8_VALUE);
//		requestPropertys.put(MyHttpHeaders.USER_AGENT, MyMediaType.USER_AGENT_IPHONE);
//		requestPropertys.put(MyHttpHeaders.APPINFO, MyMediaType.APPINFO);
//		requestPropertys.put(MyHttpHeaders.POCKET_TOKEN, token);
//		/* 请求参数 */
//		String payloadJson = "{\"nextTime\":" + String.valueOf(nextTime) + ",\"needTop1Msg\":"
//				+ String.valueOf(needTop1Msg) + ",\"roomId\":\"" + roomId + "\"}";
//		/* 发送请求 */
//		try {
//			String messageStr = https.setDataType(HttpMethod.POST.name()).setRequestProperty(requestPropertys)
//					.setPayloadJson(payloadJson).setUrl(HttpsURL.ROOM_MESSAGE_ALL).send();
//			System.out.println(messageStr);
//		} catch (KeyManagementException | NoSuchAlgorithmException | IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	@Test
//	public void threadTest() {
//		MyThread mt = new MyThread();
//		System.out.println("1:" + mt.getState().name());
//		mt.start();
//		try {
//			mt.sleep(2000l);
//			System.out.println("3:" + mt.getState().name());
//			mt.sleep(5000l);
//			System.out.println("4:" + mt.getState().name());
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		mt.run();
//	}
//
//	class MyThread extends Thread {
//
//		@Override
//		public void run() {
//			System.out.println("2:" + getState().name());
//			try {
//				sleep(5000l);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//			int i = 0;
//			while (i != 30) {
//				System.out.println("Thread(" + getName() + ") run " + ++i);
//			}
//		}
//
//	}
//
//	class MyCallable implements Callable<String> {
//
//		@Override
//		public String call() throws Exception {
//
//			return "ok";
//		}
//
//	}
//
//	@Test
//	public void threadPool() {
//		ExecutorService fixedPool = Executors.newFixedThreadPool(10, new CustomThreadFactory("Thread-"));
//		int max = 100;
//		for (int i = 0; i < max; i++) {
//			fixedPool.execute(() -> {
//				Thread t = new MyThread();
//				System.out.println("线程名称：" + t.getName() + " 线程优先级：" + t.getPriority());
//				t.start();
//			});
//		}
//		fixedPool.shutdown();
//	}
//
//	class CustomThreadFactory implements ThreadFactory {
//
//		/**
//		 * 线程名前缀
//		 */
//		private String namePrefix;
//
//		/**
//		 * 线程组
//		 */
//		private final ThreadGroup group;
//
//		/**
//		 * 线程计数
//		 */
//		private final AtomicInteger threadNumber = new AtomicInteger(0);
//
//		public CustomThreadFactory(String namePrefix) {
//			SecurityManager sm = System.getSecurityManager();
//			this.group = sm != null ? sm.getThreadGroup() : Thread.currentThread().getThreadGroup();
//			this.namePrefix = namePrefix;
//		}
//
//		@Override
//		public Thread newThread(Runnable r) {
//			// 声明线程
//			Thread thread = new Thread(group, r, namePrefix + this.threadNumber.getAndIncrement(), 0);
//
//			// 设置守护线程为false
//			if (thread.isDaemon()) {
//				thread.setDaemon(false);
//			}
//
//			// 设置线程优先级（默认为标准级）
//			if (thread.getPriority() != Thread.NORM_PRIORITY) {
//				thread.setPriority(Thread.NORM_PRIORITY);
//			}
//
//			return thread;
//		}
//
//	}
//
//}