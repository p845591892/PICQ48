package com.snh48.picq.utils;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cc.moecraft.utils.MathUtils;

/**
 * @Description: 数字工具类
 *               <p>
 *               包含各种处理数字的静态方法。
 * @author JuFF_白羽
 * @date 2019年1月8日 上午10:32:07
 */
public class MathUtil extends MathUtils{

	/**
	 * @Description: 判断字符串是否是纯数字
	 * @author JuFF_白羽
	 * @param str
	 *            待判断的字符串
	 * @return 是数字返回ture，否则返回false
	 */
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	/**
	 * @Description: 获取指定位数的随机数字
	 * @author JuFF_白羽
	 * @param digit
	 * @return 随机数字字符串
	 */
	public static String random(int digit) {
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		for (int i = 0; i < digit; i++) {
			sb.append(random.nextInt(9));
		}
		return sb.toString();
	}

}
