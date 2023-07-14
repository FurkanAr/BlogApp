package com.demo.Blog.Notification;

import com.demo.Blog.Notification.model.Mail;
import com.demo.Blog.Notification.repository.MailRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

@SpringBootApplication
public class NotificationApplication {

	private final MailRepository mailRepository;

	public NotificationApplication(MailRepository mailRepository) {
		this.mailRepository = mailRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(NotificationApplication.class, args);

	}

	@PostConstruct
	public void add(){
		Mail mail = new Mail();
		mail.setEmail("admin@gmail.com");
		mail.setMessage("welcome");
		mail.setFullName("admin");
		mail.setUserName("admin");
		mail.setCreateDate(LocalDateTime.now());
		mailRepository.save(mail);
		mailRepository.findAll().forEach(System.out :: println);


	}



}
