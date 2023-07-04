package com.demo.Blog.converter;

import com.demo.Blog.model.User;
import com.demo.Blog.response.AuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AuthConverter {

    Logger logger = LoggerFactory.getLogger(getClass());

    public AuthResponse convert(User savedUser, String token, String registration_message) {
        logger.info("convert to AuthResponse method started");
        AuthResponse authResponse = new AuthResponse();
        authResponse.setUserId(savedUser.getId());
        authResponse.setUserName(savedUser.getUserName());
        authResponse.setMessage(registration_message);
        authResponse.setToken(token);
        logger.info("convert to AuthResponse method successfully worked");
        return authResponse;
    }
}
