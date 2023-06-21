package com.demo.Blog.converter;

import com.demo.Blog.model.User;
import com.demo.Blog.response.AuthResponse;
import org.springframework.stereotype.Component;

@Component
public class AuthConverter {

    public AuthResponse convert(User savedUser, String token, String registration_message) {
        AuthResponse authResponse = new AuthResponse();
        authResponse.setUserId(savedUser.getId());
        authResponse.setUserName(savedUser.getUserName());
        authResponse.setMessage(registration_message);
        authResponse.setToken(token);
        return authResponse;
    }
}
