package com.snh48.picq.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.Deflater;
import java.util.zip.InflaterOutputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

import com.snh48.picq.https.HttpsURL;

import cn.hutool.core.codec.Base64Decoder;
import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.crypto.digest.MD5;

/**
 * @ClassName: StringUtil
 * @Description: 字符工具类
 *               <p>
 *               包含各种处理字符的静态方法。
 * @author JuFF_白羽
 * @date 2018年7月11日 下午9:08:31
 */
public class StringUtil extends StringUtils {

	private static final String POCKET_SALT = "K4bMWJawAtnyyTNOa70S";
	private static final String TAOBA_SALT = "%#54$^%&SDF^A*52#@7";

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

	/**
	 * zlib压缩
	 * 
	 * @param data 被压缩的数据
	 * @return 压缩后的数据
	 */
	public static byte[] zlibCompress(String data) {
		byte[] output = new byte[0];
		try {
			byte[] dataBytes = data.getBytes("utf-8");
			output = zlibCompress(dataBytes);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return output;
	}

	/**
	 * zlib压缩
	 * 
	 * @param data 被压缩的数据
	 * @return 压缩后的数据
	 */
	public static byte[] zlibCompress(byte[] data) {
		byte[] output = new byte[0];
		Deflater compresser = new Deflater();
		compresser.setInput(data);
		compresser.finish();
		ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);
		try {
			byte[] buf = new byte[8192];
			while (!compresser.finished()) {
				int i = compresser.deflate(buf);
				bos.write(buf, 0, i);
			}
			output = bos.toByteArray();
		} catch (Exception e) {
			output = data;
			e.printStackTrace();
		} finally {
			try {
				bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		compresser.end();
		return output;
	}

	/**
	 * zlib解压
	 * 
	 * @param data 被解压的数据
	 * @return 解压后的数据
	 */
	public static byte[] zlibDecompress(byte[] data) {
		byte[] output = new byte[0];
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		InflaterOutputStream zos = new InflaterOutputStream(bos);
		try {
			zos.write(data);
			output = bos.toByteArray();
		} catch (Exception e) {
			output = data;
			e.printStackTrace();
		} finally {
			try {
				zos.close();
				bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return output;
	}

	/**
	 * 获取口袋48接口中PA请求头的值
	 */
	public static String getPa() {
		String t = String.valueOf(System.currentTimeMillis() / 1000).concat("000");
		String r = String.valueOf(getrandom(1000, 9999));
		String tempM = t.concat(r).concat(POCKET_SALT);
		String m = MD5.create().digestHex(tempM);
		String tempPa = String.join(",", t, r, m);
		String pa = Base64Encoder.encode(tempPa);
		return pa;
	}

	private static int getrandom(int start, int end) {
		int num = (int) (Math.random() * (end - start + 1) + start);
		return num;
	}

	/**
	 * 自动补全无http或https的48资源地址（SNH48专用）
	 * 
	 * @param sourceUrl 原获取到的url
	 * @return 补全后的URL
	 */
	public static String getSourceUrl(String sourceUrl) {
		if (!sourceUrl.startsWith("http://") && !sourceUrl.startsWith("https://")) {
			sourceUrl = HttpsURL.SOURCE + sourceUrl;
		}
		return sourceUrl;
	}

	/**
	 * 编码字符串（桃叭接口请）
	 * 
	 * @param data 被编码的字符串（求参数）
	 * @return 编码后的字符串
	 */
	public static String encodePayloadJson(String data) {
		int length = data.length();
		byte[] compressed = zlibCompress(data);
		byte[] salted = addSalt(compressed);
		String result = Base64Encoder.encode(salted);
		return String.valueOf(length).concat("$").concat(result);
	}

	/**
	 * 解码字符串（桃叭接口）
	 * 
	 * @param requestResult 被解码的字符串（请求返回值）
	 * @return 解码后的字符串
	 */
	public static String decodeRequestResult(String requestResult) {
		String source = requestResult.split("\\$")[1];
		byte[] decoded = Base64Decoder.decode(source);
		byte[] salted = addSalt(decoded);
		byte[] decompressed = zlibDecompress(salted);
		String jsonStr = "";
		try {
			jsonStr = new String(decompressed, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return jsonStr;
	}

	private static byte[] addSalt(byte[] compressed) {
		char[] salt = TAOBA_SALT.toCharArray();
		for (int i = 0; i < compressed.length; i++) {
			byte ch = compressed[i];
			if (i % 2 == 0) {
				ch = (byte) (ch ^ (byte) (salt[(i / 2) % salt.length]));
			}
			compressed[i] = ch;
		}
		return compressed;
	}

	/**
	 * 判断字符串是否为数字
	 * 
	 * @param str 被验证字符串
	 * @return 如果是返回true，否则返回false
	 */
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}

}