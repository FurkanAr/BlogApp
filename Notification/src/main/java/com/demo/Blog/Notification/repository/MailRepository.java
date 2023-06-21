package com.demo.Blog.Notification.repository;

import com.demo.Blog.Notification.model.Mail;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MailRepository extends MongoRepository<Mail, String> {

}
