package com.snh48.picq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy // 启动AOP自动代理
@SpringBootApplication
public class Picq48Application {

	public static void main(String[] args) {
		SpringApplication.run(Picq48Application.class, args);
	}

}
