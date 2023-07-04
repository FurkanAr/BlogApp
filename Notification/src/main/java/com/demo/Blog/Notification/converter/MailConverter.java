package com.demo.Blog.Notification.converter;

import com.demo.Blog.Notification.model.Mail;
import com.demo.Blog.Notification.request.MailRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class MailConverter {

    Logger logger = LoggerFactory.getLogger(getClass());

    public Mail convert(MailRequest mailRequest){
        logger.info("convert to Mail method started");
        Mail mail = new Mail();
        mail.setEmail(mailRequest.getEmail());
        mail.setMessage(mailRequest.getMessage());
        mail.setFullName(mailRequest.getFullName());
        mail.setUserName(mailRequest.getUserName());
        mail.setCreateDate(LocalDateTime.now());
        logger.info("convert to Mail method successfully worked");
        return mail;

    }



}
