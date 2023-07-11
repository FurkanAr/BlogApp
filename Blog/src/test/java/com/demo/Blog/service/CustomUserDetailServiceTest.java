package com.demo.Blog.service;

import com.demo.Blog.exception.user.UserNotFoundException;
import com.demo.Blog.model.User;
import com.demo.Blog.model.enums.UserRole;
import com.demo.Blog.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class CustomUserDetailServiceTest {

    @InjectMocks
    private CustomUserDetailService customUserDetailService;
    @Mock
    private UserRepository userRepository;

    @Test
    void it_should_throw_username_not_found_exception() {

        // given

        // when
        Throwable exception = catchThrowable(() -> customUserDetailService.loadUserByUsername("tester"));

        // then
        assertThat(exception).isInstanceOf(UsernameNotFoundException.class);
    }

    @Test
    void it_should_load_user_by_username() {

        // given
        Mockito.when(userRepository.findByUserName(Mockito.anyString())).thenReturn(getUser());

        // when
        UserDetails userDetails = customUserDetailService.loadUserByUsername("tester");

        // then
        assertThat(userDetails).isNotNull();
        assertEquals(userDetails.getUsername(), getUser().get().getUserName());
        assertEquals(userDetails.getPassword(), getUser().get().getPassword());

        assertThat(userDetails.getAuthorities()).usingRecursiveFieldByFieldElementComparatorIgnoringFields(getUser().get().getRole().name());
    }

    private Optional<User> getUser() {
        return Optional.of(new User("tester", "test-user", "test@gmail.com", "Test-password123", UserRole.USER));
    }

}