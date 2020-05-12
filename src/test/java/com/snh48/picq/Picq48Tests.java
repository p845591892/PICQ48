//package com.snh48.picq;
//
//import java.io.IOException;
//import java.security.KeyManagementException;
//import java.security.NoSuchAlgorithmException;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.function.Consumer;
//import java.util.function.Function;
//import java.util.function.Predicate;
//import java.util.function.Supplier;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.http.HttpMethod;
//
//import com.snh48.picq.https.HttpsURL;
//import com.snh48.picq.https.MyHttpHeaders;
//import com.snh48.picq.https.MyMediaType;
//import com.snh48.picq.utils.Https;
//
//public class Picq48Tests {
//
//	@Test
//	public void lambdaTest() {
//		// 消费型接口 有参数，无返回
//		changeStr("hello", (str) -> System.out.println(str));
//
//		// 供给型接口 无参数，有返回
//		String value = getValue(() -> "hello");
//		System.out.println(value);
//
//		// 函数式接口 有参数，有返回
//		Long result1 = changeNum(100L, (x) -> x + 200L);
//		System.out.println(result1);
//
//		// 断言型接口 有参数，返回boolean
//		boolean result2 = changeBoolean("hello", (str) -> str.length() > 5);
//		System.out.println(result2);
//		
//	}
//
//	/**
//	 * Consumer<T> 消费型接口
//	 * 
//	 * @param str
//	 * @param con
//	 */
//	public void changeStr(String str, Consumer<String> con) {
//		con.accept(str);
//	}
//
//	/**
//	 * Supplier<T> 供给型接口
//	 * 
//	 * @param sup
//	 * @return
//	 */
//	public String getValue(Supplier<String> sup) {
//		return sup.get();
//	}
//
//	/**
//	 * Function<T,R> 函数式接口
//	 * 
//	 * @param num
//	 * @param fun
//	 * @return
//	 */
//	public Long changeNum(Long num, Function<Long, Long> fun) {
//		return fun.apply(num);
//	}
//
//	/**
//	 * Predicate<T> 断言型接口
//	 * 
//	 * @param str
//	 * @param pre
//	 * @return
//	 */
//	public boolean changeBoolean(String str, Predicate<String> pre) {
//		return pre.test(str);
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
//		String payloadJson = "{\"mobile\":\"15676101104\",\"pwd\":\"2d3m9rQmxsz34VYV\"}";
//		/* 发送请求 */
//		String loginJson = https.setUrl(HttpsURL.TOKEN).setDataType(HttpMethod.POST.name()).setPayloadJson(payloadJson)
//				.setRequestProperty(requestPropertys).send();
//		System.out.println(loginJson);
//	}
//
//	private String token = "OTN7Hto/EX7i3FZIynB4yfQ2jN+4S8FsB03BQX9B0g1F2O4tlNv9CP1oQbHqUK+1ZuDAkFmZTgYA8znPgGnZz3Sru84SBiLjiQNEWLqmWlAjDfFrYEYscvaYbZm0tjVx";
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
//	/**
//	 * 获取口袋房间消息
//	 */
//	@Test
//	public void getRoomMessage() {
//		String memberId = "19";
//		long roomId = 67333101;
//		long nextTime = 1579930408196l;
//
//		Https https = new Https();
//		/* 请求头 */
//		Map<String, String> requestPropertys = new HashMap<String, String>();
//		requestPropertys.put(MyHttpHeaders.ACCEPT, MyMediaType.ALL_VALUE);
//		requestPropertys.put(MyHttpHeaders.CONTENT_TYPE, MyMediaType.APPLICATION_JSON_UTF8_VALUE);
//		requestPropertys.put(MyHttpHeaders.USER_AGENT, MyMediaType.USER_AGENT_IPHONE);
//		requestPropertys.put(MyHttpHeaders.POCKET_TOKEN, token);
//		/* 请求参数 */
//		String payloadJson = "{\"ownerId\":\"" + memberId + "\",\"needTop1Msg\":\"false\",\"nextTime\":\""
//				+ String.valueOf(nextTime) + "\",\"roomId\":\"" + roomId + "\"}";
//		/* 发送请求 */
//		try {
//			String messageStr = https.setDataType(HttpMethod.POST.name()).setRequestProperty(requestPropertys)
//					.setPayloadJson(payloadJson).setUrl(HttpsURL.ROOM_MESSAGE).send();
//			System.out.println(messageStr);
//		} catch (KeyManagementException | NoSuchAlgorithmException | IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	@Test
//	public void https() {
//		Https https = new Https();
//		/* 请求头 */
//		Map<String, String> requestPropertys = new HashMap<String, String>();
//		requestPropertys.put(MyHttpHeaders.ACCEPT, MyMediaType.CHROME_VALUE);
//		requestPropertys.put(MyHttpHeaders.USER_AGENT, MyMediaType.CHROME_USER_AGENT);
//		/* 请求参数 */
//		Map<String, String> params = new HashMap<String, String>();
//		params.put("gid", "00");
////		params.put("callback", "get_members_success");
////		params.put("_", String.valueOf(System.currentTimeMillis()));
//		/* 发送请求 */
//		try {
//			String result = https.setDataType(HttpMethod.GET.name()).setRequestProperty(requestPropertys)
//					.setParams(params).setUrl(HttpsURL.ALL_MEMBER_LIST_V2).send();
//			System.out.println(result);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	@Test
//	public void test() {
//		// 并行流 多个线程执行
//		List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
//		numbers.parallelStream().forEach(System.out::println);
//
//		//
//		System.out.println("=========================");
//		numbers.stream().sequential().forEach(System.out::println);
//	}
//	
//	@Test
//	public void taobaHttpsTest() {
//		
//	}
//
////	@Test
////	public void threadTest() {
////		MyThread mt = new MyThread();
////		System.out.println("1:" + mt.getState().name());
////		mt.start();
////		try {
////			mt.sleep(2000l);
////			System.out.println("3:" + mt.getState().name());
////			mt.sleep(5000l);
////			System.out.println("4:" + mt.getState().name());
////		} catch (InterruptedException e) {
////			e.printStackTrace();
////		}
////		mt.run();
////	}
////
////	class MyThread extends Thread {
////
////		@Override
////		public void run() {
////			System.out.println("2:" + getState().name());
////			try {
////				sleep(5000l);
////			} catch (InterruptedException e) {
////				e.printStackTrace();
////			}
////			int i = 0;
////			while (i != 30) {
////				System.out.println("Thread(" + getName() + ") run " + ++i);
////			}
////		}
////
////	}
////
////	class MyCallable implements Callable<String> {
////
////		@Override
////		public String call() throws Exception {
////
////			return "ok";
////		}
////
////	}
////
////	@Test
////	public void threadPool() {
////		ExecutorService fixedPool = Executors.newFixedThreadPool(10, new CustomThreadFactory("Thread-"));
////		int max = 100;
////		for (int i = 0; i < max; i++) {
////			fixedPool.execute(() -> {
////				Thread t = new MyThread();
////				System.out.println("线程名称：" + t.getName() + " 线程优先级：" + t.getPriority());
////				t.start();
////			});
////		}
////		fixedPool.shutdown();
////	}
////
////	class CustomThreadFactory implements ThreadFactory {
////
////		/**
////		 * 线程名前缀
////		 */
////		private String namePrefix;
////
////		/**
////		 * 线程组
////		 */
////		private final ThreadGroup group;
////
////		/**
////		 * 线程计数
////		 */
////		private final AtomicInteger threadNumber = new AtomicInteger(0);
////
////		public CustomThreadFactory(String namePrefix) {
////			SecurityManager sm = System.getSecurityManager();
////			this.group = sm != null ? sm.getThreadGroup() : Thread.currentThread().getThreadGroup();
////			this.namePrefix = namePrefix;
////		}
////
////		@Override
////		public Thread newThread(Runnable r) {
////			// 声明线程
////			Thread thread = new Thread(group, r, namePrefix + this.threadNumber.getAndIncrement(), 0);
////
////			// 设置守护线程为false
////			if (thread.isDaemon()) {
////				thread.setDaemon(false);
////			}
////
////			// 设置线程优先级（默认为标准级）
////			if (thread.getPriority() != Thread.NORM_PRIORITY) {
////				thread.setPriority(Thread.NORM_PRIORITY);
////			}
////
////			return thread;
////		}
////
////	}
//
//}