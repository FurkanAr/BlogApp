package com.demo.Blog.service;

import com.demo.Blog.converter.UserConverter;
import com.demo.Blog.exception.messages.Messages;
import com.demo.Blog.exception.user.*;
import com.demo.Blog.model.User;
import com.demo.Blog.repository.UserRepository;
import com.demo.Blog.request.UserEmailUpdateRequest;
import com.demo.Blog.request.UserPasswordUpdateRequest;
import com.demo.Blog.request.UserUserNameUpdateRequest;
import com.demo.Blog.response.UserResponse;
import com.demo.Blog.utils.UserUtil;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserConverter userConverter, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserResponse> findAll() {
        return userConverter.convert(userRepository.findAll());
    }

    public UserResponse findById(Long userId) {
        return userConverter.convert(findUserById(userId));
    }

    public UserResponse updateUserPassword(UserPasswordUpdateRequest updateRequest) {
        User foundUser = userRepository.findByUserName(updateRequest.getUserName()).orElseThrow(() -> new UsernameNotFoundException(Messages.User.NOT_EXISTS + updateRequest.getUserName()));

        boolean isPasswordSame = passwordEncoder.matches(updateRequest.getOldPassword(), foundUser.getPassword());

        System.out.println("pass1 " + foundUser.getPassword());
        System.out.println("updateRequest " + updateRequest.getNewPassword());
        if (!isPasswordSame) {
            throw new PasswordNotCorrectException(Messages.User.INCORRECT_PASSWORD + updateRequest.getUserName());
        }

        foundUser.setPassword(passwordEncoder.encode(updateRequest.getNewPassword()));
        User user = userRepository.save(foundUser);
        return userConverter.convert(user);
    }

    public UserResponse updateUserUserName(UserUserNameUpdateRequest updateRequest) {
        User foundUser = userRepository.findByUserName(updateRequest.getOldUserName()).orElseThrow(() -> new UsernameNotFoundException(Messages.User.NOT_EXISTS + updateRequest.getOldUserName()));

        boolean existUserName = userRepository.existsUserByUserName(updateRequest.getNewUserName());

        if (existUserName) {
            throw new UserNameAlreadyInUseException(Messages.User.NAME_EXIST + updateRequest.getNewUserName());
        }
        foundUser.setUserName(updateRequest.getNewUserName());
        User user = userRepository.save(foundUser);
        return userConverter.convert(user);
    }

    public UserResponse updateUserEmail(UserEmailUpdateRequest updateRequest) {
        User foundUser = userRepository.findByEmail(updateRequest.getOldEmail()).orElseThrow(() -> new UserEmailNotFoundException(Messages.User.EMAIL_NOT_EXISTS + updateRequest.getOldEmail()));

        boolean existByEmail = userRepository.existsUserByEmail(updateRequest.getNewEmail());

        if (existByEmail) {
            throw new UserEmailAlreadyInUseException(Messages.User.EMAIL_EXIST + updateRequest.getNewEmail());
        }

        foundUser.setEmail(updateRequest.getNewEmail());
        User user = userRepository.save(foundUser);
        return userConverter.convert(user);
    }

    public String deleteUser(Long userId) {
        User user = findUserById(userId);
        userRepository.delete(user);
        return user.getId().toString();
    }

    public User findUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(Messages.User.ID_NOT_EXISTS + userId));
    }

}
