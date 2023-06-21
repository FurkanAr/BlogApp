package com.demo.blogeurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class BlogEurekaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlogEurekaServerApplication.class, args);
	}

}
