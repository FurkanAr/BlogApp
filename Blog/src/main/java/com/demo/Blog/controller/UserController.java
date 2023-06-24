package com.demo.Blog.controller;

import com.demo.Blog.request.UserEmailUpdateRequest;
import com.demo.Blog.request.UserPasswordUpdateRequest;
import com.demo.Blog.request.UserUserNameUpdateRequest;
import com.demo.Blog.response.UserResponse;
import com.demo.Blog.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    Logger logger = LoggerFactory.getLogger(getClass());

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/update/password")
    public ResponseEntity<UserResponse> updateUserPassword(@RequestBody @Valid UserPasswordUpdateRequest updateRequest) {
        logger.debug("updateUserPassword method started");
        UserResponse userResponse = userService.updateUserPassword(updateRequest);
        logger.info("updateUserPassword successfully worked, username: {}", updateRequest.getUserName());
        return ResponseEntity.ok(userResponse);
    }

    @PutMapping("/update/username")
    public ResponseEntity<UserResponse> updateUserUserName(@RequestBody @Valid UserUserNameUpdateRequest updateRequest) {
        logger.debug("updateUserUserName method started");
        UserResponse userResponse = userService.updateUserUserName(updateRequest);
        logger.info("updateUserUserName successfully worked, username: {}", updateRequest.getNewUserName());
        return ResponseEntity.ok(userResponse);
    }

    @PutMapping("/update/email")
    public ResponseEntity<UserResponse> updateUserEmail(@RequestBody @Valid UserEmailUpdateRequest updateRequest) {
        logger.debug("updateUserEmail method started");
        UserResponse userResponse = userService.updateUserEmail(updateRequest);
        logger.info("updateUserEmail successfully worked, email: {}", updateRequest.getNewEmail());
        return ResponseEntity.ok(userResponse);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        logger.debug("deleteUser method started");
        String message = userService.deleteUser(userId);
        logger.info("deleteUser successfully worked, userId: {}",userId);
        return ResponseEntity.ok(message);
    }
}
