package com.demo.Blog.service;

import com.demo.Blog.converter.UserConverter;
import com.demo.Blog.model.User;
import com.demo.Blog.repository.UserRepository;
import com.demo.Blog.request.UserRequest;
import com.demo.Blog.request.UserUpdateRequest;
import com.demo.Blog.response.UserResponse;
import org.springframework.stereotype.Service;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;

    private final JwtService jwtService;


    public UserService(UserRepository userRepository, UserConverter userConverter, JwtService jwtService) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
        this.jwtService = jwtService;
    }
    public List<UserResponse> findAll() {
        return userConverter.convert(userRepository.findAll());
    }

    public UserResponse createUser(UserRequest newUser) {
        Optional<User> foundUser = userRepository.findByEmail(newUser.getEmail());
        if (foundUser.isPresent()){
            throw new RuntimeException(
                    "User already has account by given email: " + newUser.getEmail());
        }
        User user = userRepository.save(userConverter.convert(newUser));
        return userConverter.convert(user);
    }

    public UserResponse findById(Long userId) {
        return userConverter.convert(findUserById(userId));
    }

    public UserResponse updateUser(UserUpdateRequest updateRequest) {
        User foundUser = userRepository.findByEmail(updateRequest.getEmail()).orElseThrow(() -> new RuntimeException("User bulunamadı: "+ updateRequest.getEmail()));
        updateCurrentUser(foundUser,updateRequest);
        User user = userRepository.save(foundUser);
        return userConverter.convert(user);
    }

    private void updateCurrentUser(User currentUser, UserUpdateRequest updateRequest){
        currentUser.setUserName(updateRequest.getUserName());
        currentUser.setPassword(updateRequest.getPassword());
        currentUser.setFullName(updateRequest.getFullName());
        currentUser.setEmail(updateRequest.getEmail());
    }

    public String deleteUser(Long userId) {
        User user = findUserById(userId);
        userRepository.delete(user);
        return user.getId().toString();
    }

    public User findUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User bulunamadı: "+ userId));
        return user;
    }

    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String header = request.getHeader("Authorization");
        final String jwt;
        final String userName;

        if (header == null || !header.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }
        jwt = header.substring(7);
        userName = jwtService.findUserName(jwt);

        filterChain.doFilter(request, response);
    }

}
