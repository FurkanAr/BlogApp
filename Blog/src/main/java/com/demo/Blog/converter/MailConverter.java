package com.demo.Blog.converter;

import com.demo.Blog.model.User;
import com.demo.Blog.request.MailRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MailConverter {

    Logger logger = LoggerFactory.getLogger(getClass());

    public MailRequest convert(User user, String message){
        logger.info("convert to MailRequest method started");
        MailRequest mailRequest = new MailRequest();
        mailRequest.setEmail(user.getEmail());
        mailRequest.setUserName(user.getUserName());
        mailRequest.setFullName(user.getFullName());
        mailRequest.setMessage(message);
        logger.info("convert to MailRequest method successfully worked");
        return mailRequest;
    }
}
