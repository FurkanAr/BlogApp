package com.demo.Blog.controller;

import com.demo.Blog.config.auth.JwtAuthenticationFilter;
import com.demo.Blog.model.enums.UserRole;
import com.demo.Blog.request.UserEmailUpdateRequest;
import com.demo.Blog.request.UserPasswordUpdateRequest;
import com.demo.Blog.request.UserUserNameUpdateRequest;
import com.demo.Blog.response.UserResponse;
import com.demo.Blog.service.JwtService;
import com.demo.Blog.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @MockBean
    private UserService userService;
    private ObjectMapper mapper = new ObjectMapper();

    @Test
    void it_should_update_user_password() throws Exception {

        // given
        Mockito.when(userService.updateUserPassword(Mockito.any(UserPasswordUpdateRequest.class)))
                .thenReturn(getUserPasswordUpdateMessage());

        String request = mapper.writeValueAsString(getUserPasswordUpdateRequest());

        // when
        ResultActions resultActions = mockMvc.perform(put("/users/update/password")
                .content(request).contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value( "Your password is updated"));
    }

    @Test
    void it_should_update_user_user_name() throws Exception {

        // given
        Mockito.when(userService.updateUserUserName(Mockito.any(UserUserNameUpdateRequest.class)))
                .thenReturn(getUserResponse());

        String request = mapper.writeValueAsString(getUserUserNameUpdateRequest());

        // when
        ResultActions resultActions = mockMvc.perform(put("/users/update/username")
                .content(request).contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.userName").value("tester"))
                .andExpect(jsonPath("$.fullName").value("test-user"))
                .andExpect(jsonPath("$.email").value("test@gmail.com"))
                .andExpect(jsonPath("$.role").value(UserRole.USER.toString()));
    }

    @Test
    void it_should_update_user_email() throws Exception {
        // given
        Mockito.when(userService.updateUserEmail(Mockito.any(UserEmailUpdateRequest.class)))
                .thenReturn(getUserResponse());

        String request = mapper.writeValueAsString(getUserEmailUpdateRequest());

        // when
        ResultActions resultActions = mockMvc.perform(put("/users/update/email")
                .content(request).contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.userName").value("tester"))
                .andExpect(jsonPath("$.fullName").value("test-user"))
                .andExpect(jsonPath("$.email").value("test@gmail.com"))
                .andExpect(jsonPath("$.role").value(UserRole.USER.toString()));
    }

    @Test
    void it_should_delete_user_by_id() throws Exception {

        // given
        Mockito.when(userService.deleteUser(Mockito.any(Long.class)))
                .thenReturn(getDeleteMessage(1L));

        // when
        ResultActions resultActions = mockMvc.perform(delete("/users/1"));

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$").value(1));

    }

    private String getDeleteMessage(Long id) {
        return id.toString();
    }

    private UserEmailUpdateRequest getUserEmailUpdateRequest() {
        return new UserEmailUpdateRequest("tester@gmail.com", "test@gmail.com");
    }

    private UserUserNameUpdateRequest getUserUserNameUpdateRequest() {
        return new UserUserNameUpdateRequest("tester123", "tester");
    }

    private String getUserPasswordUpdateMessage() {
        return "Your password is updated";
    }

    private UserPasswordUpdateRequest getUserPasswordUpdateRequest() {
        return new UserPasswordUpdateRequest("tester", "Updated-password123", "Test-password123");
    }

    private UserResponse getUserResponse() {
        return new UserResponse(1L, "tester", "test-user", "test@gmail.com", UserRole.USER);
    }
}