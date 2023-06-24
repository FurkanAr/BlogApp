package com.demo.Blog.controller;

import com.demo.Blog.request.LoginRequest;
import com.demo.Blog.request.UserRequest;
import com.demo.Blog.response.AuthResponse;
import com.demo.Blog.service.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    Logger logger = LoggerFactory.getLogger(getClass());

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> save(@RequestBody @Valid UserRequest userRequest) {
        logger.debug("save method started");
        AuthResponse authResponse = authenticationService.save(userRequest);
        logger.info("save successfully worked, user email: {}", userRequest.getEmail());
        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        logger.debug("login method started");
        AuthResponse authResponse = authenticationService.login(loginRequest);
        logger.info("login successfully worked, username: {}", loginRequest.getUserName());
        return ResponseEntity.ok(authResponse);
    }
}
