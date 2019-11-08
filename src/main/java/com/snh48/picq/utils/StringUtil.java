package com.snh48.picq.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

/**
 * @ClassName: StringUtil
 * @Description: 字符工具类
 *               <p>
 *               包含各种处理字符的静态方法。
 * @author JuFF_白羽
 * @date 2018年7月11日 下午9:08:31
 */
public class StringUtil extends StringUtils {

	/**
	 * @Title: removeNonBmpUnicode
	 * @Description: 过滤掉特殊编码的文字表情，例如emoji表情
	 *               <p>
	 *               在升级为3.X版本后，已支持emoji，故该方法已不再使用。
	 * @author JuFF_白羽
	 * @param str 需要过滤的字符串
	 * @return String 过滤后的字符串
	 */
	@Deprecated
	public static String removeNonBmpUnicode(String str) {
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (isPunctuation(c) || isUserDefined(c))
				continue;
			else {
				if (!isChinese(c)) {
					str = str.replace(c, '?');
				}
			}
		}
		return str;
	}

	private static boolean isChinese(char c) {
		Character.UnicodeScript sc = Character.UnicodeScript.of(c);
		if (sc == Character.UnicodeScript.HAN) {
			return true;
		}
		return false;
	}

	public static boolean isPunctuation(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if ( // punctuation, spacing, and formatting characters
		ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				// symbols and punctuation in the unified Chinese, Japanese and
				// Korean script
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				// fullwidth character or a halfwidth character
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
				// vertical glyph variants for east Asian compatibility
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS
				// vertical punctuation for compatibility characters with the
				// Chinese Standard GB 18030
				|| ub == Character.UnicodeBlock.VERTICAL_FORMS
				// ascii
				|| ub == Character.UnicodeBlock.BASIC_LATIN) {
			return true;
		} else {
			return false;
		}
	}

	private static Boolean isUserDefined(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.NUMBER_FORMS || ub == Character.UnicodeBlock.ENCLOSED_ALPHANUMERICS
				|| ub == Character.UnicodeBlock.LETTERLIKE_SYMBOLS || c == '\ufeff' || c == '\u00a0')
			return true;
		return false;
	}

	/**
	 * @Description: shiro密码处理
	 *               <p>
	 *               使用MD5方式加密，获得密码的密文。
	 * @author JuFF_白羽
	 * @param salt     盐
	 * @param password 密码明文
	 * @return String MD5加密后的密码
	 */
	public static String shiroMd5(String salt, String password) {
		String hashAlgorithmName = "MD5";// 加密方式
		ByteSource saltByte = ByteSource.Util.bytes(salt);// 盐值
		int hashIterations = 1024;// 加密1024次
		SimpleHash hash = new SimpleHash(hashAlgorithmName, password, saltByte, hashIterations);
		return hash.toString();
	}

	/**
	 * 判断字符串是否是只含英文
	 * 
	 * <pre>
	 * StringUtil.isChinese("")  = true
	 * StringUtil.isChinese("  ")   = false
	 * StringUtil.isChinese("你好") = false
	 * StringUtil.isChinese("abc")  = true
	 * StringUtil.isChinese("ABC")  = true
	 * StringUtil.isChinese("abc你好") = false
	 * </pre>
	 * 
	 * @param charaString 要判断的字符串
	 * @return 是返回true，否返回false
	 */
	public static boolean isEnglish(String charaString) {
		return charaString.matches("^[a-zA-Z]*");
	}

	/**
	 * 判断字符串是否是含有中文
	 * 
	 * <pre>
	 * StringUtil.isChinese("")  = false
	 * StringUtil.isChinese("  ")   = false
	 * StringUtil.isChinese("abc")  = false
	 * StringUtil.isChinese("你好") = true
	 * StringUtil.isChinese("abc你好") = true
	 * </pre>
	 * 
	 * @param str 要判断的字符串
	 * @return 是返回true，否返回false
	 */
	public static boolean isChinese(String str) {
		String regEx = "[\\u4e00-\\u9fa5]+";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		if (m.find())
			return true;
		else
			return false;
	}

}
