package com.demo.bloggateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class BlogGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlogGatewayApplication.class, args);
	}

}
