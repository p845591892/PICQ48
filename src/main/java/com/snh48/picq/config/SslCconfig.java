//package com.snh48.picq.config;
//
//import org.apache.catalina.Context;
//import org.apache.catalina.connector.Connector;
//import org.apache.tomcat.util.descriptor.web.SecurityCollection;
//import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
//import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * SSL证书配置类
// * 
// * @author shiro
// *
// */
//@Configuration
//public class SslCconfig {
//
//	/**
//	 * 自定义森林狼连接器，指定监听http端口的访问转入https指定的端口。
//	 */
//	@Bean
//	public Connector connector() {
//		Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
//		connector.setScheme("http");
//		connector.setPort(80);
//		connector.setSecure(false);
//		connector.setRedirectPort(443);
//		return connector;
//	}
//
//	/**
//	 * 把自定义连接器注入tomcat的web服务工厂
//	 * 
//	 * @param connector 自定义的连接器
//	 */
//	@Bean
//	public TomcatServletWebServerFactory tomcatServletWebServerFactory(Connector connector) {
//		TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
//			@Override
//			protected void postProcessContext(Context context) {
//				SecurityConstraint securityConstraint = new SecurityConstraint();
//				securityConstraint.setUserConstraint("CONFIDENTIAL");
//				SecurityCollection collection = new SecurityCollection();
//				collection.addPattern("/*");
//				securityConstraint.addCollection(collection);
//				context.addConstraint(securityConstraint);
//			}
//		};
//		tomcat.addAdditionalTomcatConnectors(connector);
//		return tomcat;
//	}
//
//}
