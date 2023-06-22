package com.demo.Blog.service;

import com.demo.Blog.exception.messages.Messages;
import com.demo.Blog.repository.UserRepository;
import com.demo.Blog.security.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUserName(username)
                .map(CustomUserDetails :: new)
                .orElseThrow(() -> new UsernameNotFoundException(Messages.User.NOT_EXISTS + username));
    }
}
