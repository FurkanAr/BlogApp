package com.demo.Blog.controller;

import com.demo.Blog.config.auth.JwtAuthenticationFilter;
import com.demo.Blog.model.enums.UserRole;
import com.demo.Blog.request.LoginRequest;
import com.demo.Blog.request.UserRequest;
import com.demo.Blog.response.AuthResponse;
import com.demo.Blog.service.AuthenticationService;
import com.demo.Blog.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AuthenticationService authenticationService;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    private ObjectMapper mapper = new ObjectMapper();

    @Test
    void it_should_login() throws Exception {
        // given
        Mockito.when(authenticationService.login(Mockito.any(LoginRequest.class))).thenReturn(getAuthResponse());

        String request = mapper.writeValueAsString(getLoginRequest());

        // when
        ResultActions resultActions = mockMvc.perform(post("/auth/login")
                .content(request).contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.message").value("test message"))
                .andExpect(jsonPath("$.userName").value("tester"))
                .andExpect(jsonPath("$.token").value("test-token"));

    }

    @Test
    void it_should_register() throws Exception {
        // given
        Mockito.when(authenticationService.save(Mockito.any(UserRequest.class))).thenReturn(getAuthResponse());

        String request = mapper.writeValueAsString(getUserRequest());

        // when
        ResultActions resultActions = mockMvc.perform(post("/auth/register")
                .content(request).contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.message").value("test message"))
                .andExpect(jsonPath("$.userName").value("tester"))
                .andExpect(jsonPath("$.token").value("test-token"));
    }

    @Test
    void it_should_throw_validation_exception() throws Exception {
        // given
        Mockito.when(authenticationService.save(Mockito.any(UserRequest.class))).thenReturn(getAuthResponse());

        String body = mapper.writeValueAsString(getInvalidUserRequest());

        // when
        ResultActions resultActions = mockMvc.perform(post("/auth/register")
                .content(body).contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(jsonPath("$.message").value("Validation Failed"));
    }

    private LoginRequest getLoginRequest() {
        return new LoginRequest("tester", "Test-password123");
    }

    private UserRequest getInvalidUserRequest() {
        return new UserRequest("test", "test-user", "test@gmail.com",
                "Test-password", UserRole.ADMIN.toString());
    }

    private UserRequest getUserRequest() {
        return new UserRequest("tester", "test-user", "test@gmail.com",
                "Test-password123", UserRole.ADMIN.toString());
    }

    private AuthResponse getAuthResponse() {
        return new AuthResponse(1L, "test message", "tester", "test-token");
    }

}