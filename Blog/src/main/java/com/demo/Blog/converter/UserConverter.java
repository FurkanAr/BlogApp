package com.demo.Blog.converter;

import com.demo.Blog.model.User;
import com.demo.Blog.model.enums.UserRole;
import com.demo.Blog.request.UserRequest;
import com.demo.Blog.response.UserResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserConverter {

    private final PasswordEncoder passwordEncoder;

    public UserConverter(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    public UserResponse convert (User user){
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setUserName(user.getUserName());
        userResponse.setFullName(user.getFullName());
        userResponse.setEmail(user.getEmail());
        userResponse.setRole(user.getRole());
        return userResponse;
    }

    public User convert (UserRequest userRequest){
        User user = new User();
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setUserName(userRequest.getUserName());
        user.setEmail(userRequest.getEmail());
        user.setRole(UserRole.valueOf(userRequest.getRole()));
        user.setFullName(userRequest.getFullName());
        user.setCreateDate(LocalDateTime.now());
        return user;
    }

    public List<UserResponse> convert (List<User> userList){
        List<UserResponse> userResponses = new ArrayList<>();
        userList.stream().forEach(user -> userResponses.add(convert(user)));
        return userResponses;
    }


}
