package com.demo.Blog.service;

import com.demo.Blog.config.rabbitMQ.RabbitMQMailConfiguration;
import com.demo.Blog.converter.AuthConverter;
import com.demo.Blog.converter.MailConverter;
import com.demo.Blog.converter.UserConverter;
import com.demo.Blog.exception.messages.Messages;
import com.demo.Blog.exception.user.UserEmailAlreadyInUseException;
import com.demo.Blog.model.User;
import com.demo.Blog.repository.UserRepository;
import com.demo.Blog.request.LoginRequest;
import com.demo.Blog.request.MailRequest;
import com.demo.Blog.request.UserRequest;
import com.demo.Blog.response.AuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final UserConverter userConverter;
    private final RabbitTemplate rabbitTemplate;
    private final RabbitMQMailConfiguration rabbitMQMailConfiguration;
    private final MailConverter mailConverter;
    private final AuthenticationManager authenticationManager;
    private final AuthConverter authConverter;
    Logger logger = LoggerFactory.getLogger(getClass());
    @Value("${registration.mail.message}")
    private String REGISTRATION_MAIL_MESSAGE;
    @Value("${registration.message}")
    private String REGISTRATION_MESSAGE;
    @Value("${login.message}")
    private String LOGIN_MESSAGE;

    public AuthenticationService(UserRepository userRepository, JwtService jwtService, UserConverter userConverter, RabbitTemplate rabbitTemplate, RabbitMQMailConfiguration rabbitMQMailConfiguration, MailConverter mailConverter, AuthenticationManager authenticationManager, AuthConverter authConverter) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.userConverter = userConverter;
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitMQMailConfiguration = rabbitMQMailConfiguration;
        this.mailConverter = mailConverter;
        this.authenticationManager = authenticationManager;
        this.authConverter = authConverter;
    }
    @Transactional
    public AuthResponse save(UserRequest userRequest) {
        logger.info("save method started");
        logger.info("UserRequest: {}", userRequest);

        Optional<User> foundUser = userRepository.findByEmail(userRequest.getEmail());
        if (foundUser.isPresent()) {
            logger.warn("User already has account by given email: {}", userRequest.getEmail());
            throw new UserEmailAlreadyInUseException(Messages.User.EXIST + userRequest.getEmail());
        }

        User savedUser = userRepository.save(userConverter.convert(userRequest));
        logger.info("User created: {}", savedUser.getId());

        MailRequest mailRequest = mailConverter.convert(savedUser, REGISTRATION_MAIL_MESSAGE);
        rabbitTemplate.convertAndSend(rabbitMQMailConfiguration.getQueueName(), mailRequest);
        logger.info("MailRequest: {}, sent to : {}", mailRequest, rabbitMQMailConfiguration.getQueueName());

        var token = jwtService.generateToken(savedUser);
        logger.info("Token created for user: {}, token: {}", savedUser.getId(), token);

        logger.info("save method successfully worked");
        return authConverter.convert(savedUser, token, REGISTRATION_MESSAGE);
    }

    public AuthResponse login(LoginRequest loginRequest) {
        logger.info("login method started");
        User foundUser = userRepository.findByUserName(loginRequest.getUserName()).orElseThrow(()
                -> new UsernameNotFoundException(Messages.User.NOT_EXISTS + loginRequest.getUserName()));
        logger.info("Found user: {}", foundUser.getId());

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword());

        authenticationManager.authenticate(authToken);
        logger.info("User authenticated user: {}", foundUser.getId());

        var token = jwtService.generateToken(foundUser);
        logger.info("User {}, authenticated with token: {} ", foundUser.getId(), token);

        logger.info("login method successfully worked");
        return authConverter.convert(foundUser, token, LOGIN_MESSAGE);
    }
}
