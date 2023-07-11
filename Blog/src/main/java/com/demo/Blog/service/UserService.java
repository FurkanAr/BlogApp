package com.demo.Blog.service;

import com.demo.Blog.constants.Constant;
import com.demo.Blog.converter.UserConverter;
import com.demo.Blog.exception.messages.Messages;
import com.demo.Blog.exception.user.*;
import com.demo.Blog.model.User;
import com.demo.Blog.repository.UserRepository;
import com.demo.Blog.request.UserEmailUpdateRequest;
import com.demo.Blog.request.UserPasswordUpdateRequest;
import com.demo.Blog.request.UserUserNameUpdateRequest;
import com.demo.Blog.response.UserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final PasswordEncoder passwordEncoder;

    Logger logger = LoggerFactory.getLogger(getClass());

    public UserService(UserRepository userRepository, UserConverter userConverter, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
        this.passwordEncoder = passwordEncoder;
    }

    protected List<UserResponse> findAll() {
        logger.info("findAll method started");
        List<User> users = userRepository.findAll();
        logger.info("findAll method successfully worked");
        return userConverter.convert(users);
    }

    protected UserResponse findById(Long userId) {
        logger.info("findById method started");
        logger.info("findById method successfully worked");
        return userConverter.convert(findUserById(userId));
    }

    @Transactional
    public String updateUserPassword(UserPasswordUpdateRequest updateRequest) {
        logger.info("updateUserPassword method started");

        User foundUser = getUser(updateRequest.getUserName());

        boolean isPasswordSame = passwordEncoder.matches(updateRequest.getOldPassword(), foundUser.getPassword());
        logger.info("Passwords are matched: {} ", isPasswordSame);

        if (!isPasswordSame) {
            logger.warn("User: {}, password is incorrect", foundUser.getId());
            throw new PasswordNotCorrectException(Messages.User.INCORRECT_PASSWORD + updateRequest.getUserName());
        }

        foundUser.setPassword(passwordEncoder.encode(updateRequest.getNewPassword()));
        User user = userRepository.save(foundUser);
        logger.info("User password updated: {} ", user.getId());

        logger.info("updateUserPassword method successfully worked");
        return Constant.User.PASSWORD_UPDATED;
    }

    @Transactional
    public UserResponse updateUserUserName(UserUserNameUpdateRequest updateRequest) {
        logger.info("updateUserUserName method started");
        logger.info("UserUserNameUpdateRequest: {} ", updateRequest);

        User foundUser = getUser(updateRequest.getOldUserName());

        boolean existUserName = userRepository.existsUserByUserName(updateRequest.getNewUserName());
        logger.info("Username is exist: {}", existUserName);

        if (existUserName) {
            logger.warn("Username is already in use: {}", updateRequest.getNewUserName());
            throw new UserNameAlreadyInUseException(Messages.User.NAME_EXIST + updateRequest.getNewUserName());
        }
        foundUser.setUserName(updateRequest.getNewUserName());
        User user = userRepository.save(foundUser);
        logger.info("Username updated: {} ", user.getId());

        logger.info("updateUserUserName method successfully worked");
        return userConverter.convert(user);
    }


    @Transactional
    public UserResponse updateUserEmail(UserEmailUpdateRequest updateRequest) {
        logger.info("updateUserEmail method started");
        logger.info("UserEmailUpdateRequest: {} ", updateRequest);

        User foundUser = userRepository.findByEmail(updateRequest.getOldEmail()).orElseThrow(() -> new UserEmailNotFoundException(Messages.User.EMAIL_NOT_EXISTS + updateRequest.getOldEmail()));
        logger.info("Found user: {}, email: {}", foundUser.getId(), updateRequest.getOldEmail());

        boolean existByEmail = userRepository.existsUserByEmail(updateRequest.getNewEmail());
        logger.info("Email is exist: {}", existByEmail);

        if (existByEmail) {
            logger.warn("Email is already in use: {}", updateRequest.getNewEmail());
            throw new UserEmailAlreadyInUseException(Messages.User.EMAIL_EXIST + updateRequest.getNewEmail());
        }

        foundUser.setEmail(updateRequest.getNewEmail());
        User user = userRepository.save(foundUser);
        logger.info("Email updated: {} ", user.getId());

        logger.info("updateUserEmail method successfully worked");
        return userConverter.convert(user);
    }

    public String deleteUser(Long userId) {
        logger.info("deleteUser method started");

        User user = findUserById(userId);
        userRepository.delete(user);
        logger.info("User deleted: {} ", userId);

        logger.info("deleteUser method successfully worked");
        return user.getId().toString();
    }

    protected User findUserById(Long userId) {
        logger.info("findUserById method started");

        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(Messages.User.ID_NOT_EXISTS + userId));
        logger.info("Found user by userId: {} ", userId);

        logger.info("findUserById method successfully worked");
        return user;
    }

    protected Long findNumberOfUsers() {
        logger.info("findNumberOfUsers method started");

        Long total = userRepository.count();
        logger.info("Number of users: {} ", total);

        logger.info("findNumberOfUsers method successfully worked");
        return total;
    }

    User getUser(String userName) {
        logger.info("getUser method started");

        User foundUser = userRepository.findByUserName(userName).orElseThrow(() -> new UsernameNotFoundException(Messages.User.NOT_EXISTS + userName));
        logger.info("Found user: {}, by username: {} ", foundUser.getId(), userName);

        logger.info("getUser method successfully worked");
        return foundUser;
    }
}
