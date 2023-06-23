package com.demo.Blog.controller;

import com.demo.Blog.request.UserEmailUpdateRequest;
import com.demo.Blog.request.UserPasswordUpdateRequest;
import com.demo.Blog.request.UserUserNameUpdateRequest;
import com.demo.Blog.response.UserResponse;
import com.demo.Blog.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/update/password")
    public ResponseEntity<UserResponse> updateUserPassword(@RequestBody UserPasswordUpdateRequest updateRequest) {
        UserResponse userResponse = userService.updateUserPassword(updateRequest);
        return ResponseEntity.ok(userResponse);
    }

    @PutMapping("/update/username")
    public ResponseEntity<UserResponse> updateUserUserName(@RequestBody UserUserNameUpdateRequest updateRequest) {
        UserResponse userResponse = userService.updateUserUserName(updateRequest);
        return ResponseEntity.ok(userResponse);
    }

    @PutMapping("/update/email")
    public ResponseEntity<UserResponse> updateUserEmail(@RequestBody UserEmailUpdateRequest updateRequest) {
        UserResponse userResponse = userService.updateUserEmail(updateRequest);
        return ResponseEntity.ok(userResponse);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        String message = userService.deleteUser(userId);
        return ResponseEntity.ok(message);
    }
}
