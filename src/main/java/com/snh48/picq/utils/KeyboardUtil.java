package com.snh48.picq.utils;

import java.awt.Image;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.io.IOException;

/**
 * @Description: 键盘工具类
 *               <p>
 *               模拟键盘操作的工具。
 * @author JuFF_白羽
 * @date 2018年7月12日 下午6:08:30
 */
public class KeyboardUtil {
	public static void main(String[] args) throws Exception {
		Robot robot = new Robot();
		// 调用系统方法打开记事本
		Runtime.getRuntime().exec("notepad");
		robot.delay(2000);
		// 全屏显示
		// keyPressWithAlt(robot,KeyEvent.VK_SPACE);
		// 输入x
		keyPress(robot, KeyEvent.VK_X);
		// 输入回车
		keyPress(robot, KeyEvent.VK_ENTER);
		robot.delay(1000);
		// 输入字符串
		keyPressString(robot, "哈哈哈哈哈哈哈哈哈嗝");
	}

	/**
	 * @Description: Shift组合键
	 * @author JuFF_白羽
	 * @param r
	 * @param key
	 */
	public static void keyPressWithShift(Robot r, int key) {
		// 按下Shift
		r.keyPress(KeyEvent.VK_SHIFT);
		// 按下某个键
		r.keyPress(key);

		// 释放某个键
		r.keyRelease(key);
		// 释放Shift
		r.keyRelease(KeyEvent.VK_SHIFT);
		// 等待100ms
		r.delay(100);
	}

	/**
	 * @Description: Ctrl组合键
	 * @author JuFF_白羽
	 * @param r
	 * @param key
	 */
	public static void keyPressWithCtrl(Robot r, int key) {
		r.keyPress(KeyEvent.VK_CONTROL);
		r.keyPress(key);
		r.keyRelease(key);
		r.keyRelease(KeyEvent.VK_CONTROL);
		r.delay(100);
	}

	/**
	 * @Description: Alt组合键
	 * @author JuFF_白羽
	 * @param r
	 * @param key
	 */
	public static void keyPressWithAlt(Robot r, int key) {
		r.keyPress(KeyEvent.VK_ALT);
		r.keyPress(key);
		r.keyRelease(key);
		r.keyRelease(KeyEvent.VK_ALT);
		r.delay(100);
	}

	/**
	 * @Title: keyPressString
	 * @Description: 将文本输入到文本框中
	 *               <p>
	 *               实现原理是使用剪切板，将字符串参数放入剪切板中，然后模拟粘贴（Ctrl+V）。
	 * @author JuFF_白羽
	 * @param r   Java的自动化系统输入事件对象
	 * @param str 要写入文本框的字符串
	 */
	public static void keyPressString(Robot r, String str) {
		// 获取剪切板
		Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
		// 将传入字符串封装下
		Transferable tText = new StringSelection(str);
		// 将字符串放入剪切板
		clip.setContents(tText, null);
		// 按下Ctrl+V实现粘贴文本
		keyPressWithCtrl(r, KeyEvent.VK_V);
		r.delay(100);
	}

	/**
	 * @Description: 输入数字
	 * @author JuFF_白羽
	 * @param r
	 * @param number
	 */
	public static void keyPressNumber(Robot r, int number) {
		// 将数字转成字符串
		String str = Integer.toString(number);
		// 调用字符串的方法
		keyPressString(r, str);
	}

	/**
	 * @Description: 按一次某个按键
	 * @author JuFF_白羽
	 * @param r
	 * @param key
	 * @return void 返回类型
	 */
	public static void keyPress(Robot r, int key) {
		// 按下键
		r.keyPress(key);
		// 释放键
		r.keyRelease(key);
		r.delay(100);
	}

	/**
	 * @Description: 快速打开QQ消息(这个组合键因人而异)
	 *               <p>
	 *               这里使用的方法是ctrl+alt+z来弹出QQ消息
	 * @author JuFF_白羽
	 * @param r
	 */
	public static void keyPressAtlWithCtrlWithZ(Robot r) {
		r.keyPress(KeyEvent.VK_ALT);
		r.keyPress(KeyEvent.VK_CONTROL);
		r.keyPress(KeyEvent.VK_Z);
		r.keyRelease(KeyEvent.VK_Z);
		r.keyRelease(KeyEvent.VK_CONTROL);
		r.keyRelease(KeyEvent.VK_ALT);
		r.delay(100);
	}

	/**
	 * @Description: 点击一下鼠标左键
	 * @author JuFF_白羽
	 * @param r
	 */
	public static void mouseLeftHit(Robot r) {
		r.mousePress(KeyEvent.BUTTON1_DOWN_MASK);
		r.mouseRelease(KeyEvent.BUTTON1_DOWN_MASK);
		r.delay(100);
	}

	/**
	 * @Description: 图片内部类
	 *               <p>
	 *               此处继承Transferable，作用为封装图片超类Images，封装后可放入剪切板中。
	 * @author JuFF_白羽
	 * @date 2018年12月24日 下午7:13:33
	 */
	public static class Images implements Transferable {

		private Image image; // 得到图片或者图片流

		public Images(Image image) {
			this.image = image;
		}

		@Override
		public DataFlavor[] getTransferDataFlavors() {
			return new DataFlavor[] { DataFlavor.imageFlavor };
		}

		@Override
		public boolean isDataFlavorSupported(DataFlavor flavor) {
			return DataFlavor.imageFlavor.equals(flavor);
		}

		@Override
		public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
			if (!DataFlavor.imageFlavor.equals(flavor)) {
				throw new UnsupportedFlavorException(flavor);
			}
			return image;
		}

	}

	/**
	 * @Description: 将图片输入到文本框中
	 *               <p>
	 *               实现原理是使用剪切板，将图片类参数放入剪切板中，然后模拟粘贴（Ctrl+V）。
	 * @author JuFF_白羽
	 * @param r     Java的自动化系统输入事件对象
	 * @param image 图片图形超类
	 */
	public static void keyPressImage(Robot r, Image image) {
		// 获取剪切板
		Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
		// 将传入图片超类封装下
		Images imgSel = new Images(image);
		// 将图片放入剪切板
		clip.setContents(imgSel, null);
		// 按下Ctrl+V实现粘贴图片
		keyPressWithCtrl(r, KeyEvent.VK_V);
		// 等1.5秒
		r.delay(1500);
	}

}