package com.snh48.picq.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName: DateUtil
 * @Description: 时间相关工具类
 * @author JuFF_白羽
 * @date 2018年3月19日 上午9:22:04
 */
public class DateUtil {

	public DateUtil() {
	}

	/**
	 * @Title: getDate
	 * @Description: 获取当前日期时间 yyyy-MM-dd HH:mm:ss
	 * @author JuFF_白羽
	 * @return String 返回类型
	 */
	public static String getDate() {
		return getDate(new Date());
	}

	/**
	 * @Title: getDate
	 * @Description: 获取yyyy-MM-dd HH:mm:ss格式的日期时间
	 * @author JuFF_白羽
	 * @param date
	 * @return String 返回类型
	 */
	public static String getDate(Date date) {
		return getDate(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * @Title: getDate
	 * @Description: 获取指定格式的时间字符串
	 * @author JuFF_白羽
	 * @param date   时间对象
	 * @param format 时间格式
	 * @return String 返回类型
	 */
	public static String getDate(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	/**
	 * @Title: countDay
	 * @Description: 获取当前时间指定变动天数后的时间字符串
	 *               <p>
	 *               默认格式：yyyy-MM-ddHH:mm:ss
	 * @author JuFF_白羽
	 * @param amount 变化的天数
	 *               <p>
	 *               整数往后推,负数往前推
	 */
	public static String countDay(int amount) {
		return countDay("yyyy-MM-ddHH:mm:ss", amount);
	}

	/**
	 * @Title: countDay
	 * @Description: 获取当前时间指定变动天数后的指定格式时间字符串
	 * @author JuFF_白羽
	 * @param format 时间格式
	 * @param amount 变化的天数
	 *               <p>
	 *               整数往后推,负数往前推
	 */
	public static String countDay(String format, int amount) {
		Date date = new Date();// 取时间
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, amount);
		date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
		return getDate(date, format);
	}

	/**
	 * 
	 * @Title: getDateFormat
	 * @Description: 根据字符串获取Date类型数据,默认字符串格式yyyy-MM-ddHH:mm:ss
	 * @author JuFF_白羽
	 * @param date 时间字符串
	 * @return Date 返回类型
	 * @throws ParseException
	 */
	public static Date getDateFormat(String date) throws ParseException {
		return getDateFormat(date, "yyyy-MM-ddHH:mm:ss");
	}

	/**
	 * @Title: getDateFormat
	 * @Description: 将时间戳转换成Date
	 * @author JuFF_白羽
	 * @param timestamp 间戳转
	 * @throws ParseException
	 * @return Date 返回类型
	 */
	public static Date getDateFormat(Long timestamp) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
		String d = sdf.format(timestamp);
		Date date = sdf.parse(d);
		return date;
	}

	/**
	 * @Title: getDateFormat
	 * @Description: 将指定格式的时间字符串转换成时间对象
	 * @author JuFF_白羽
	 * @param date   时间字符串
	 * @param format 时间格式
	 * @throws ParseException
	 * @return Date 返回类型
	 */
	public static Date getDateFormat(String date, String format) throws ParseException {
		/*
		 * 去回车、TAB、换行
		 */
		if (date.equals("") || date.equals(" ")) {
			return null;
		} else {
			Pattern p = Pattern.compile("\\s*\t|\r|\n");
			Matcher m = p.matcher(date);
			date = m.replaceAll("");

			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.parse(date);
		}
	}

	/**
	 * @Title: countMin
	 * @Description: 获取当前时间指定变动分钟后的指定格式时间字符串
	 * @author JuFF_白羽
	 * @param date 时间对象
	 * @param min  改变的分钟（正数时间后推，负数时间往前推）
	 * @return String 返回类型
	 */
	public static String countMin(String format, int min) {
		Date date = new Date();// 取时间
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, min);
		date = calendar.getTime();
		return getDate(date, format);
	}

	/**
	 * @Description: 获取当日00:00:00的时间对象
	 * @author JuFF_白羽
	 * @return Date 返回类型
	 */
	public static Date getMidnight() {
		return getMidnight(new Date());
	}

	/**
	 * @Description: 获取指定日期00:00:00的时间对象
	 * @author JuFF_白羽
	 * @param date 时间对象
	 * @return Date 返回类型
	 */
	public static Date getMidnight(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0,
				0, 0);
		return calendar.getTime();
	}

	/**
	 * @Description: 获取当日23:59:59的时间对象
	 * @author JuFF_白羽
	 * @return Date 返回类型
	 */
	public static Date getNearMidnight() {
		return getNearMidnight(new Date());
	}

	/**
	 * @Description: 获取指定日期23:59:59的时间对象
	 * @author JuFF_白羽
	 * @param date 时间对象
	 * @return Date 返回类型
	 */
	public static Date getNearMidnight(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 23,
				59, 59);
		return calendar.getTime();
	}

}
