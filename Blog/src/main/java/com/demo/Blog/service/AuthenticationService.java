package com.demo.Blog.service;

import com.demo.Blog.config.rabbitMQ.RabbitMQMailConfiguration;
import com.demo.Blog.converter.AuthConverter;
import com.demo.Blog.converter.MailConverter;
import com.demo.Blog.converter.UserConverter;
import com.demo.Blog.exception.user.UserEmailAlreadyInUseException;
import com.demo.Blog.model.User;
import com.demo.Blog.repository.UserRepository;
import com.demo.Blog.request.LoginRequest;
import com.demo.Blog.request.MailRequest;
import com.demo.Blog.request.UserRequest;
import com.demo.Blog.response.AuthResponse;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {
    @Value("${registration.mail.message}")
    private String REGISTRATION_MAIL_MESSAGE;
    @Value("${registration.message}")
    private String REGISTRATION_MESSAGE;
    @Value("${login.message}")
    private String LOGIN_MESSAGE;

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final UserConverter userConverter;
    private final RabbitTemplate rabbitTemplate;
    private final RabbitMQMailConfiguration rabbitMQMailConfiguration;
    private final MailConverter mailConverter;
    private final AuthenticationManager authenticationManager;
    private final AuthConverter authConverter;

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

    public AuthResponse save(UserRequest userRequest) {
        Optional<User> foundUser = userRepository.findByEmail(userRequest.getEmail());
        if (foundUser.isPresent())
            throw new UserEmailAlreadyInUseException("User already has account by given email: "
                    + userRequest.getEmail());

        User savedUser = userRepository.save(userConverter.convert(userRequest));

        MailRequest mailRequest = mailConverter.convert(savedUser, REGISTRATION_MAIL_MESSAGE);

        rabbitTemplate.convertAndSend(rabbitMQMailConfiguration.getQueueName(), mailRequest);

        var token = jwtService.generateToken(savedUser);

        AuthResponse authResponse = authConverter.convert(savedUser, token, REGISTRATION_MESSAGE);

        return authResponse;
    }


    public AuthResponse login(LoginRequest loginRequest) {
        User foundUser = userRepository.findByUserName(loginRequest.getUserName())
                .orElseThrow(() -> new UsernameNotFoundException("User cannot find with given username: "
                        + loginRequest.getUserName()));

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword());

        authenticationManager.authenticate(authToken);

        var token = jwtService.generateToken(foundUser);

        AuthResponse authResponse = authConverter.convert(foundUser, token, LOGIN_MESSAGE);

        return authResponse;
    }
}
