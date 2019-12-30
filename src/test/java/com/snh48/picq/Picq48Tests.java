//package com.snh48.picq;
//
//import java.io.IOException;
//import java.security.KeyManagementException;
//import java.security.NoSuchAlgorithmException;
//import java.util.concurrent.Callable;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ThreadFactory;
//import java.util.concurrent.atomic.AtomicInteger;
//import java.util.regex.Pattern;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.BeanUtils;
//
//import com.snh48.picq.entity.snh48.Member;
//import com.snh48.picq.https.HttpsPICQ48;
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
//		try {
//			System.out.println(HttpsPICQ48.httpsTrip(0, 0, false));
//		} catch (KeyManagementException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (NoSuchAlgorithmException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//	@Test
//	public void test2() {
//		String regex = "北京|上海|广州";
//		String str1 = "北京";
//		String str2 = "上海";
//		String str3 = "广州";
//		String str4 = "湖南";
//		System.out.println(Pattern.matches(regex, str1));
//		System.out.println(Pattern.matches(regex, str2));
//		System.out.println(Pattern.matches(regex, str3));
//		System.out.println(Pattern.matches(regex, str4));
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