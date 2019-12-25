//package com.snh48.picq;
//
//import java.util.concurrent.Callable;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ThreadFactory;
//import java.util.concurrent.atomic.AtomicInteger;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.BeanUtils;
//
//import com.snh48.picq.entity.snh48.Member;
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
//	@Test
//	public void httpsTest() {
//		String text = "<audio>️https://nim.nosdn.127.net/NDA5MzEwOA==/bmltYV83MjUxODk2NTEyXzE1NzY4NjAzMzkyMDJfOTQ4Mjc4MWUtYzEyOC00ZDZjLWJiMGMtOGRjYjc0NGI3NmVi";
//		String[] urlArray = text.split("<audio>");
//		String url = urlArray[1];
//		Https https = new Https();
//		try {
//			https.setUrl(url).downloadFile("/Users/shiro/Downloads", DateUtil.getDate("yyyyMMddHHmmss") + ".mp3");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	@Test
//	public void test2() {
//		String test = "abcdefg";
//		String test2 = "abc";
//		System.out.println(test.startsWith(test2));
//		System.out.println(test2.startsWith(test));
//
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
//				Thread t = Thread.currentThread();
//				System.out.println("线程名称：" + t.getName() + " 线程优先级：" + t.getPriority());
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