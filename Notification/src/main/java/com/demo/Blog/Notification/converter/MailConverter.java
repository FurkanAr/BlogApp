package com.demo.Blog.Notification.converter;

import com.demo.Blog.Notification.model.Mail;
import com.demo.Blog.Notification.request.MailRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class MailConverter {

    public Mail convert(MailRequest mailRequest){
        Mail mail = new Mail();
        mail.setEmail(mailRequest.getEmail());
        mail.setMessage(mailRequest.getMessage());
        mail.setFullName(mailRequest.getFullName());
        mail.setUserName(mailRequest.getUserName());
        mail.setCreateDate(LocalDateTime.now());
        return mail;

    }



}
