package com.snh48.picq.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

public class IpUtil {

	public static String getIpAddr(HttpServletRequest request) {
		String ipAddress = request.getHeader("x-forwarded-for");
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getRemoteAddr();
			String localIp = "127.0.0.1";
			String localIpv6 = "0:0:0:0:0:0:0:1";
			if (ipAddress.equals(localIp) || ipAddress.equals(localIpv6)) {
				// 根据网卡取本机配置的IP
				InetAddress inet = null;
				try {
					inet = InetAddress.getLocalHost();
					ipAddress = inet.getHostAddress();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
			}
		}
		// 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
		String ipSeparate = ",";
		int ipLength = 15;
		if (ipAddress != null && ipAddress.length() > ipLength) {
			if (ipAddress.indexOf(ipSeparate) > 0) {
				ipAddress = ipAddress.substring(0, ipAddress.indexOf(ipSeparate));
			}
		}
		return ipAddress;
	}

}
