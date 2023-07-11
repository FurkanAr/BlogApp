package com.demo.Blog.service;

import com.demo.Blog.exception.messages.Messages;
import com.demo.Blog.repository.UserRepository;
import com.demo.Blog.security.CustomUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;
    Logger logger = LoggerFactory.getLogger(getClass());

    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        logger.info("loadUserByUsername method started");
        UserDetails user = userRepository.findByUserName(username)
                .map(CustomUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException(Messages.User.NOT_EXISTS + username));
        logger.info("User found with username: {}", username);
        logger.info("loadUserByUsername method successfully worked");
        return user;
    }
}
