package com.demo.Blog.service;

import com.demo.Blog.config.rabbitMQ.RabbitMQMailConfiguration;
import com.demo.Blog.converter.AuthConverter;
import com.demo.Blog.converter.MailConverter;
import com.demo.Blog.converter.UserConverter;
import com.demo.Blog.exception.user.UserEmailAlreadyInUseException;
import com.demo.Blog.model.User;
import com.demo.Blog.model.enums.UserRole;
import com.demo.Blog.repository.UserRepository;
import com.demo.Blog.request.LoginRequest;
import com.demo.Blog.request.MailRequest;
import com.demo.Blog.request.UserRequest;
import com.demo.Blog.response.AuthResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationService authenticationService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserConverter userConverter;
    @Mock
    private AuthConverter authConverter;
    @Mock
    private MailConverter mailConverter;
    @Mock
    private RabbitTemplate rabbitTemplate;
    @Mock
    private RabbitMQMailConfiguration rabbitMQMailConfiguration;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;

    @Test
    void it_should_throw_user_email_already_in_use_exception() {

        // given
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(getUser());

        // when
        Throwable exception = catchThrowable(() -> authenticationService.save(getUserRequest()));

        // then
        assertThat(exception).isInstanceOf(UserEmailAlreadyInUseException.class);
        verify(userRepository, times(0)).save(Mockito.any(User.class));
    }

    @Test
    void it_should_save() {

        // given
        User user = createdUser();
        user.setId(1L);

        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
        Mockito.when(userConverter.convert(Mockito.any(UserRequest.class))).thenReturn(new User());
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        Mockito.when(mailConverter.convert(Mockito.any(User.class), Mockito.anyString())).thenReturn(getMailRequest());
        Mockito.when(rabbitMQMailConfiguration.getQueueName()).thenReturn("mail.queue.name");

        Mockito.when(jwtService.generateToken(Mockito.any(User.class))).thenReturn(getToken());
        Mockito.when(authConverter.convert(Mockito.any(User.class), Mockito.anyString(), Mockito.anyString())).thenReturn(getAuthResponse());

        // when
        AuthResponse authResponse = authenticationService.save(getUserRequest());

        // then
        assertThat(authResponse).isNotNull();
        assertThat(authResponse.getUserName()).isEqualTo(createdUser().getUserName());
        assertThat(authResponse.getToken()).isEqualTo(getToken());

        verify(rabbitTemplate, times(1)).convertAndSend(Mockito.anyString(), Mockito.any(MailRequest.class));
        verify(userRepository).save(Mockito.any(User.class));
    }

    @Test
    void it_should_throw_username_not_found_exception() {

        // given

        // when
        Throwable exception = catchThrowable(() -> authenticationService.login(new LoginRequest()));

        // then
        assertThat(exception).isInstanceOf(UsernameNotFoundException.class);
    }

    @Test
    void it_should_login() {

        // given
        Optional<User> user = getUser();
        user.get().setId(1L);
        Mockito.when(userRepository.findByUserName(Mockito.anyString())).thenReturn(user);
        Mockito.when(jwtService.generateToken(Mockito.any(User.class))).thenReturn(getToken());
        Mockito.when(authConverter.convert(Mockito.any(User.class), Mockito.anyString(), Mockito.anyString())).thenReturn(getAuthResponse());

        // when
        AuthResponse authResponse = authenticationService.login(getLoginRequest());

        // then
        assertThat(authResponse).isNotNull();
        assertThat(authResponse.getUserName()).isEqualTo(getUser().get().getUserName());
        assertThat(authResponse.getToken()).isEqualTo(getToken());
    }

    private LoginRequest getLoginRequest() {
        return new LoginRequest("tester", "Test-password123");
    }

    private MailRequest getMailRequest() {
        return new MailRequest("tester", "test@gmail.com" ,"test-user", "test message");
    }

    private String getToken() {
        return "test-token";
    }

    private Optional<User> getUser() {
        return Optional.of(new User("tester", "test-user", "test@gmail.com", "Test-password123", UserRole.USER));
    }

    private User createdUser() {
        return new User("tester", "test-user", "test@gmail.com", "Test-password123", UserRole.USER);
    }

    private UserRequest getUserRequest() {
        return new UserRequest("tester", "test-user", "test@gmail.com",
                "Test-password123", UserRole.ADMIN.toString());
    }

    private AuthResponse getAuthResponse() {
        return new AuthResponse(1L, "test message", "tester", "test-token");
    }

}