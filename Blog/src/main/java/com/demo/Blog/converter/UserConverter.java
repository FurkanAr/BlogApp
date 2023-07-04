package com.demo.Blog.converter;

import com.demo.Blog.model.User;
import com.demo.Blog.model.enums.UserRole;
import com.demo.Blog.request.UserRequest;
import com.demo.Blog.response.UserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserConverter {

    private final PasswordEncoder passwordEncoder;
    Logger logger = LoggerFactory.getLogger(getClass());

    public UserConverter(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse convert(User user) {
        logger.info("convert to Response method started");
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setUserName(user.getUserName());
        userResponse.setFullName(user.getFullName());
        userResponse.setEmail(user.getEmail());
        userResponse.setRole(user.getRole());
        logger.info("convert to Response method successfully worked");
        return userResponse;
    }

    public User convert(UserRequest userRequest) {
        logger.info("convert to User method started");
        User user = new User();
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setUserName(userRequest.getUserName());
        user.setEmail(userRequest.getEmail());
        user.setRole(UserRole.valueOf(userRequest.getRole()));
        user.setFullName(userRequest.getFullName());
        user.setCreateDate(LocalDateTime.now());
        logger.info("convert to User method successfully worked");
        return user;
    }

    public List<UserResponse> convert(List<User> userList) {
        logger.info("convert userList to userResponses method started");
        List<UserResponse> userResponses = new ArrayList<>();
        userList.forEach(user -> userResponses.add(convert(user)));
        logger.info("convert userList to userResponses method successfully worked");
        return userResponses;
    }

}
