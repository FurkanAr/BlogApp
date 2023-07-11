package com.demo.Blog.service;

import com.demo.Blog.model.User;
import com.demo.Blog.model.enums.UserRole;
import com.demo.Blog.security.CustomUserDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    @Test
    void it_should_find_user_name() {

        // given

        // when
        String response = jwtService.findUserName(getToken());

        // then
        assertThat(response).isNotNull();
        assertEquals(response, getUser().getUserName());
    }

    @Test
    void it_should_token_control() {

        // given

        // when
        Boolean response = jwtService.tokenControl(getToken(), getUserDetails());

        // then
        assertThat(response).isNotNull();
    }

    @Test
    void it_should_generate_token() {

        // given
        User user = getUser();
        user.setId(1L);
        // when
        String response = jwtService.generateToken(user);

        // then
        assertThat(response).isNotNull();
    }

    private UserDetails getUserDetails() {
        return new CustomUserDetails(getUser());
    }

    private String getToken() {
        return "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0ZXIiLCJpc3MiOiJCbG9nLmFwcCIsInJvbGVzIjoiQURNSU4iLCJ1c2VySWQiOiIxMDYiLCJlbWFpbCI6InRlc3RAZ21haWwuY29tIiwiaWF0IjoxNjg5MDc4MjI4LCJleHAiOjE2ODkwNzk2Njh9.4QbT5XE944i_3_WJnMQuRTkm63Ah4HqWdpMFgXKaHYA";
    }

    private User getUser() {
        return new User("tester", "test-user", "test@gmail.com", "Test-password123", UserRole.USER);
    }

}