package com.demo.Blog.utils;

import com.demo.Blog.model.User;
import com.demo.Blog.model.enums.UserRole;
import com.demo.Blog.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

@Component
public class DataLoad  {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public DataLoad(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void postConstruct(){
        User admin = new User();
        admin.setUserName("admin");
        admin.setFullName("admin");
        admin.setEmail("admin@gmail.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRole(UserRole.ADMIN);
        admin.setCreateDate(LocalDateTime.now());

        User user = new User();
        user.setUserName("user");
        user.setFullName("user");
        user.setEmail("user@gmail.com");
        user.setPassword(passwordEncoder.encode("user123"));
        user.setRole(UserRole.USER);
        user.setCreateDate(LocalDateTime.now());

        userRepository.save(admin);
        userRepository.save(user);

    }
}
