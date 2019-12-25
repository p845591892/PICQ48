package com.snh48.picq.utils;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import com.snh48.picq.config.MyX509TrustManager;
import com.snh48.picq.exception.RuleException;

import cn.hutool.http.ssl.TrustAnyHostnameVerifier;
import lombok.extern.log4j.Log4j2;

/**
 * @ClassName: Https
 * @Description: Https操作的工具类
 * @author JuFF_白羽
 * @date 2018年6月12日 上午10:39:42
 */
@Log4j2
public class Https {

	/**
	 * 请求URL
	 */
	private String url;

	/**
	 * 携带参数
	 */
	private Map<String, String> params;

	/**
	 * 请求类型
	 */
	private String dataType = "GET";

	/**
	 * 请求头
	 */
	private Map<String, String> requestPropertys;

	/**
	 * 携带json参数
	 */
	private String payloadJson;

	public Https() {
	}

	/**
	 * 刷新Https类
	 * 
	 * @param url
	 * @param dataType
	 * @param params
	 * @param requestPropertys
	 * @param payloadJson
	 */
	public void refreshHttps(String url, String dataType, Map<String, String> params,
			Map<String, String> requestPropertys, String payloadJson) {
		this.url = url;
		this.dataType = dataType;
		this.params = params;
		this.requestPropertys = requestPropertys;
		this.payloadJson = payloadJson;
	}

	/**
	 * @Title: getUrl
	 * @Description: 获取当前使用的URL
	 * @author JuFF_白羽
	 * @return String
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @Title: setUrl
	 * @Description: 设置URL
	 * @author JuFF_白羽
	 * @param url 请求地址
	 * @return Https
	 */
	public Https setUrl(String url) {
		this.url = url;
		return this;
	}

	/**
	 * @Title: getParams
	 * @Description: 获取当前使用的参数
	 * @author JuFF_白羽
	 * @return Map<String,String>
	 */
	public Map<String, String> getParams() {
		return params;
	}

	/**
	 * @Title: setParams
	 * @Description: 设置参数
	 * @author JuFF_白羽
	 * @param params 参数键值对
	 * @return Https
	 */
	public Https setParams(Map<String, String> params) {
		this.params = params;
		return this;
	}

	/**
	 * @Title: setDataType
	 * @Description: 设置请求方式
	 *               <p>
	 *               可用POST、GET等，默认是GET
	 * @author JuFF_白羽
	 * @param dataType
	 * @return Https 返回类型
	 */
	public Https setDataType(String dataType) {
		this.dataType = dataType;
		return this;
	}

	/**
	 * @Title: setRequestProperty
	 * @Description: 设置请求头
	 * @author JuFF_白羽
	 * @param requestPropertys 请求头的Map
	 * @return Https 返回类型
	 */
	public Https setRequestProperty(Map<String, String> requestPropertys) {
		this.requestPropertys = requestPropertys;
		return this;
	}

	/**
	 * @Title: setPayloadJson
	 * @Description: 设置需要用流写入的json参数
	 *               <p>
	 *               该方法是为了无法用url?携带参数的请求而编写的，使用输出流将参数写入后台请求中。
	 * @author JuFF_白羽
	 * @param payloadJson json字符串
	 * @return Https 返回类型
	 */
	public Https setPayloadJson(String payloadJson) {
		this.payloadJson = payloadJson;
		return this;
	}

	/**
	 * @Title: send
	 * @Description: 发送请求
	 * @author JuFF_白羽
	 * @return String 返回类型
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 * @throws IOException
	 */
	public String send() throws NoSuchAlgorithmException, KeyManagementException, IOException {
		this.url = validateRule(this.url);
		StringBuffer buffer = null;
		// 以SSL的规则创建SSLContext
		SSLContext sslContext = SSLContext.getInstance("SSL");
		TrustManager[] tm = { new MyX509TrustManager() };
		// 初始化
		sslContext.init(null, tm, new SecureRandom());
		// 获取SSLSocketFactory对象
		SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
		URL url = new URL(installParams(this.url, this.params));
		HttpsURLConnection urlConn;
		BufferedReader br = null;
		InputStreamReader isr = null;
		InputStream is = null;
		try {
			urlConn = (HttpsURLConnection) url.openConnection();
			urlConn.setDoOutput(true);
			urlConn.setDoInput(true);
			// 请求不使用缓存
			urlConn.setUseCaches(false);
			// 设置请求头
			if (requestPropertys != null) {
				for (Map.Entry<String, String> entry : requestPropertys.entrySet()) {
					urlConn.setRequestProperty(entry.getKey(), entry.getValue());
				}
			}
			// 设置请求方式
			urlConn.setRequestMethod(this.dataType);
			// 设置当前实例使用的SSLSoctetFactory
			urlConn.setSSLSocketFactory(sslSocketFactory);
			// 设置需要用流写入的请求参数
			if (payloadJson != null && !payloadJson.equals("")) {
				OutputStreamWriter writer = new OutputStreamWriter(urlConn.getOutputStream(), "UTF-8");
				writer.write(payloadJson);
				writer.close();
			}
			urlConn.connect();
			// 读取服务器端返回的内容
			is = urlConn.getInputStream();
			isr = new InputStreamReader(is, "utf-8");
			br = new BufferedReader(isr);
			buffer = new StringBuffer();
			String line = null;
			while ((line = br.readLine()) != null) {
				buffer.append(line);
			}
		} finally {
			try {
				if (br != null) {
					br.close();
				}
				if (isr != null) {
					isr.close();
				}
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		refreshHttps(null, null, null, null, null);// 清空参数
		return buffer.toString();
	}

	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
		InputStream is = new URL(url).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONObject json = new JSONObject(jsonText);
			return json;
		} finally {
			is.close();
		}
	}

	/**
	 * @Title: validateRule
	 * @Description: 对传入的URL进行必要的校验
	 * @author JuFF_白羽
	 * @param url 请求地址
	 */
	public static String validateRule(String url) {
		if (StringUtil.isBlank(url)) {
			log.error("url不能为空！");
			throw new RuleException("url不能为空！");
		}

		int http = url.indexOf("http://");
		int https = url.indexOf("https://");

		if (http == -1 && https == -1) {
			log.error("url的格式不正确！未含有 http:// 或 https://");
			throw new RuleException("url的格式不正确！未含有 http:// 或 https://");
		}

		if (http > 0 || https > 0) {
			log.info("URL头包含有BOM等其他字符。");
			url = removeBom(url);
		}

		return url;
	}

	/**
	 * 去除URL开头的BOM
	 * 
	 * @param url 连接字符串
	 * @return 去除了BOM的字符串
	 */
	public static String removeBom(String url) {
		if (url.startsWith("http")) {
			return url;
		}
		url = url.substring(1);
		return removeBom(url);
	}

	/**
	 * @Title: installParams
	 * @Description: 为请求地址增加参数
	 * @author JuFF_白羽
	 * @param url    请求地址
	 * @param params 装有参数的Map
	 * @return String 用?带参数的url
	 */
	private static String installParams(String url, Map<String, String> params) {
		if (params == null) {
			return url;
		}
		int flag = 1;
		url += "?";
		for (Map.Entry<String, String> param : params.entrySet()) {
			if (flag > 1) {
				url += "&";
			}
			url = url + param.getKey() + "=" + param.getValue();
			flag += 1;
		}
		return url;
	}

	/**
	 * @Description: 根据图片网络地址,发送https请求获取一个Image对象
	 * @author JuFF_白羽
	 * @return Image 图形图像超类
	 * @throws IOException
	 */
	public Image getImage() throws IOException {
		this.url = validateRule(this.url);
		InputStream inputStream = null;
		try {
			HttpURLConnection connection = null;
			connection = (HttpsURLConnection) new URL(this.url).openConnection();
			connection.setReadTimeout(10000);
			connection.setConnectTimeout(10000);
			connection.setRequestMethod("GET");
			if (connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
				inputStream = connection.getInputStream();
				return ImageIO.read(inputStream);
			} else {
				log.info("请求网络图片失败：{}", connection.getResponseCode());
			}
		} catch (IOException e) {
			log.info(e.getMessage());
		} catch (Exception e) {
			log.info(e.getMessage());
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
			refreshHttps(null, null, null, null, null);// 清空参数
		}
		return null;
	}

	/**
	 * 下载文件到指定位置
	 * 
	 * @param savePath 存放目录
	 * @param fileName 文件名
	 * @return 文件名
	 * @throws Exception
	 */
	public String downloadFile(String savePath, String fileName) throws Exception {
		this.url = validateRule(this.url);
		FileOutputStream fos = null;
		InputStream inputStream = null;
		File file = new File(savePath + File.separator + fileName);
		try {
			// 创建SSLContext
			SSLContext sslContext = SSLContext.getInstance("SSL");
			TrustManager[] tm = { new MyX509TrustManager() };
			// 初始化
			sslContext.init(null, tm, new SecureRandom());
			// 获取SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();
			// url对象
			URL url = new URL(this.url);
			// 打开连接
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			/**
			 * 这一步的原因: 当访问HTTPS的网址。您可能已经安装了服务器证书到您的JRE的keystore
			 * 但是服务器的名称与证书实际域名不相等。这通常发生在你使用的是非标准网上签发的证书。
			 * 
			 * 解决方法：让JRE相信所有的证书和对系统的域名和证书域名。
			 * 
			 * 如果少了这一步会报错:java.io.IOException: HTTPS hostname wrong: should be <localhost>
			 */
			conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
			// 设置一些参数
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			// 设置当前实例使用的SSLSoctetFactory
			conn.setSSLSocketFactory(ssf);
			conn.connect();

			// 得到输入流
			inputStream = conn.getInputStream();
			byte[] getData = readInputStream(inputStream);
			// 文件保存位置
			File saveDir = new File(savePath);
			if (!saveDir.exists()) {
				saveDir.mkdirs();
			}
			// 输出流

			fos = new FileOutputStream(file);
			fos.write(getData);
		} finally {
			if (fos != null) {
				fos.close();
			}
			if (inputStream != null) {
				inputStream.close();
			}
		}
		return file.getName();
	}

	/**
	 * 从输入流中获取字节数组
	 * 
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	private byte[] readInputStream(InputStream inputStream) throws IOException {
		byte[] b = new byte[1024];
		int len = 0;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		while ((len = inputStream.read(b)) != -1) {
			bos.write(b, 0, len);
		}
		bos.close();
		return bos.toByteArray();
	}

}
