package com.demo.Blog.converter;

import com.demo.Blog.model.User;
import com.demo.Blog.request.MailRequest;
import org.springframework.stereotype.Component;

@Component
public class MailConverter {

    public MailRequest convert(User user, String message){
        MailRequest mailRequest = new MailRequest();
        mailRequest.setEmail(user.getEmail());
        mailRequest.setUserName(user.getUserName());
        mailRequest.setFullName(user.getFullName());
        mailRequest.setMessage(message);
        return mailRequest;
    }
}
