package com.demo.Blog.service;

import com.demo.Blog.converter.UserConverter;
import com.demo.Blog.exception.user.*;
import com.demo.Blog.model.User;
import com.demo.Blog.model.enums.UserRole;
import com.demo.Blog.repository.UserRepository;
import com.demo.Blog.request.UserEmailUpdateRequest;
import com.demo.Blog.request.UserPasswordUpdateRequest;
import com.demo.Blog.request.UserUserNameUpdateRequest;
import com.demo.Blog.response.UserResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;

@ExtendWith(SpringExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserConverter userConverter;

    @Mock
    private UserRepository userRepository;

     @Mock
     private PasswordEncoder encoder;


    @Test
    void it_should_find_all() {

        // given
        Mockito.when(userRepository.findAll()).thenReturn(getUsers());
        Mockito.when(userConverter.convert(Mockito.anyList())).thenReturn(getAllUserResponses());

        // when
        List<UserResponse> userResponses = userService.findAll();

        // then
        assertThat(userResponses).isNotNull();
        assertThat(userResponses.get(0).getUserName()).isEqualTo(getUser().getUserName());
        assertThat(userResponses.get(0).getEmail()).isEqualTo(getUser().getEmail());
        assertThat(userResponses.get(0).getRole()).isEqualTo(getUser().getRole());
    }

    @Test
    void it_should_throw_user_found_exception_when_user_is_null() {

        // given

        // when
        Throwable exception = catchThrowable(() -> userService.findById(1L));

        // then
        assertThat(exception).isInstanceOf(UserNotFoundException.class);

    }

    @Test
    void it_should_find_by_id() {

        // given
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(getUser()));
        Mockito.when(userConverter.convert(Mockito.any(User.class))).thenReturn(getUserResponse());

        // when
        UserResponse userResponse = userService.findById(1L);

        // then
        assertThat(userResponse).isNotNull();
        assertThat(userResponse.getUserName()).isEqualTo(getUser().getUserName());
        assertThat(userResponse.getEmail()).isEqualTo(getUser().getEmail());
        assertThat(userResponse.getRole()).isEqualTo(getUser().getRole());
    }

    @Test
    void it_should_throw_username_not_found_exception() {

        // given

        // when
        Throwable exception = catchThrowable(() -> userService.updateUserPassword(getUserPasswordUpdateRequest()));

        // then
        assertThat(exception).isInstanceOf(UsernameNotFoundException.class);

    }

    @Test
    void it_should_throw_password_not_matched_exception() {

        // given
        Mockito.when(userRepository.findByUserName(Mockito.anyString())).thenReturn(Optional.of(getUser()));

        // when
        Throwable exception = catchThrowable(() -> userService.updateUserPassword(getUserPasswordUpdateRequest()));

        // then
        assertThat(exception).isInstanceOf(PasswordNotCorrectException.class);
        Mockito.verify(userRepository, times(0)).save(Mockito.any(User.class));

    }

    @Test
    void it_should_update_user_password(){

        // given
        Mockito.when(userRepository.findByUserName(Mockito.anyString())).thenReturn(Optional.of(getUser()));
        Mockito.when(encoder.matches(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn((getUpdatedUser()));

        // when
        String result = userService.updateUserPassword(getUserPasswordUpdateRequest());

        // then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo("Your password is updated");
        assertThat(getUserPasswordUpdateRequest().getNewPassword()).isEqualTo(getUpdatedUser().getPassword());
        Mockito.verify(userRepository, times(1)).save(Mockito.any(User.class));

    }

    @Test
    void it_should_throw_username_not_found_exception_update_username() {

        // given

        // when
        Throwable exception = catchThrowable(() -> userService.updateUserUserName(getUserUserNameUpdateRequest()));

        // then
        assertThat(exception).isInstanceOf(UsernameNotFoundException.class);

    }

    @Test
    void it_should_throw_username_already_in_use_exception() {

        // given
        Mockito.when(userRepository.findByUserName(Mockito.anyString())).thenReturn(Optional.of(getUser()));
        Mockito.when(userRepository.existsUserByUserName(Mockito.anyString())).thenReturn(true);

        // when
        Throwable exception = catchThrowable(() -> userService.updateUserUserName(getUserUserNameUpdateRequest()));

        // then
        assertThat(exception).isInstanceOf(UserNameAlreadyInUseException.class);

    }

    @Test
    void it_should_update_user_username() {

        // given
        Mockito.when(userRepository.findByUserName(Mockito.anyString())).thenReturn(Optional.of(getUser()));
        Mockito.when(userRepository.existsUserByUserName(Mockito.anyString())).thenReturn(false);
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn((getUpdatedUser()));
        Mockito.when(userConverter.convert(Mockito.any(User.class))).thenReturn(getUpdatedUserResponse());

        // when
        UserResponse userResponse = userService.updateUserUserName(getUserUserNameUpdateRequest());

        // then
        assertThat(userResponse).isNotNull();
        assertThat(userResponse.getUserName()).isEqualTo(getUpdatedUser().getUserName());
        Mockito.verify(userRepository, times(1)).save(Mockito.any(User.class));

    }

    @Test
    void it_should_throw_user_email_not_found_exception() {

        // given

        // when
        Throwable exception = catchThrowable(() -> userService.updateUserEmail(getUserEmailUpdateRequest()));

        // then
        assertThat(exception).isInstanceOf(UserEmailNotFoundException.class);
    }

    @Test
    void it_should_throw_user_email_already_in_use_exception() {

        // given
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(getUser()));
        Mockito.when(userRepository.existsUserByEmail(Mockito.anyString())).thenReturn(true);

        // when
        Throwable exception = catchThrowable(() -> userService.updateUserEmail(getUserEmailUpdateRequest()));

        // then
        assertThat(exception).isInstanceOf(UserEmailAlreadyInUseException.class);
        Mockito.verify(userRepository, times(0)).save(Mockito.any(User.class));
    }


    @Test
    void it_should_update_user_email() {

        // given
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(getUser()));
        Mockito.when(userRepository.existsUserByEmail(Mockito.anyString())).thenReturn(false);
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn((getUpdatedUser()));
        Mockito.when(userConverter.convert(Mockito.any(User.class))).thenReturn(getUpdatedUserResponse());

        // when
        UserResponse userResponse = userService.updateUserEmail(getUserEmailUpdateRequest());

        // then
        assertThat(userResponse).isNotNull();
        assertThat(userResponse.getEmail()).isEqualTo(getUpdatedUser().getEmail());
        Mockito.verify(userRepository, times(1)).save(Mockito.any(User.class));

    }

    @Test
    void it_should_throw_user_found_exception_when_user_is_null_delete_user() {

        // given

        // when
        Throwable exception = catchThrowable(() -> userService.deleteUser(1L));

        // then
        assertThat(exception).isInstanceOf(UserNotFoundException.class);
        Mockito.verify(userRepository, times(0)).delete(Mockito.any(User.class));

    }
    @Test
    void deleteUser() {

        // given
        User user = getUser();
        user.setId(1L);
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));

        // when
        String response = userService.deleteUser(1L);

        // then
        assertThat(response).isNotNull();
        assertThat(response).isEqualTo(user.getId().toString());
        Mockito.verify(userRepository, times(1)).delete(Mockito.any(User.class));

    }


    @Test
    void it_should_throw_user_found_exception_when_user_is_null_find_user_by_id() {

        // given

        // when
        Throwable exception = catchThrowable(() -> userService.findUserById(1L));

        // then
        assertThat(exception).isInstanceOf(UserNotFoundException.class);

    }

    @Test
    void it_should_find_user_by_id() {

        // given
        User user = getUser();
        user.setId(1L);
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));

        // when
        User foundUser = userService.findUserById(1L);

        // then
        assertThat(foundUser).isNotNull();
        assertEquals(foundUser.getId(), 1L);
        assertThat(foundUser.getId()).isEqualTo(user.getId());
        assertThat(foundUser.getUserName()).isEqualTo(user.getUserName());
        assertThat(foundUser.getEmail()).isEqualTo(user.getEmail());

    }

    @Test
    void it_should_find_number_of_users() {

        // given
        Mockito.when(userRepository.count()).thenReturn((long) getUsers().size());

        // when
        Long total = userService.findNumberOfUsers();

        // then
        assertThat(total).isNotNull();
        assertThat(total).isEqualTo(getUsers().size());

    }

    @Test
    void it_should_throw_username_not_found_exception_get_user() {

        // given

        // when
        Throwable exception = catchThrowable(() -> userService.getUser("tester"));

        // then
        assertThat(exception).isInstanceOf(UsernameNotFoundException.class);

    }

    @Test
    void it_should_get_user() {

        // given
        User user = getUser();
        user.setId(1L);
        Mockito.when(userRepository.findByUserName(Mockito.anyString())).thenReturn(Optional.of(user));

        // when
        User foundUser = userService.getUser("tester");

        // then
        assertThat(foundUser).isNotNull();
        assertEquals(foundUser.getId(), 1L);
        assertThat(foundUser.getUserName()).isEqualTo(user.getUserName());
        assertThat(foundUser.getEmail()).isEqualTo(user.getEmail());

    }

    private UserEmailUpdateRequest getUserEmailUpdateRequest() {
        return new UserEmailUpdateRequest("test@gmail.com", "updated-test@gmail.com");
    }

    private UserResponse getUpdatedUserResponse() {
        return new UserResponse(1L, "updated-tester", "test-user", "updated-test@gmail.com", UserRole.USER);

    }

    private UserUserNameUpdateRequest getUserUserNameUpdateRequest() {
        return new UserUserNameUpdateRequest("tester", "updated-tester");
    }

    private User getUpdatedUser() {
        return new User("updated-tester", "test-user", "updated-test@gmail.com", "Updated-password123", UserRole.USER);
    }

    private UserPasswordUpdateRequest getUserPasswordUpdateRequest() {
        return new UserPasswordUpdateRequest("tester", "Updated-password123", "Test-password123");
    }

    private List<User> getUsers() {
        return List.of(getUser());
    }

    private User getUser() {
        return new User("tester", "test-user", "test@gmail.com", "Test-password123", UserRole.USER);
    }

    private List<UserResponse> getAllUserResponses() {
        return List.of(getUserResponse());
    }

    private UserResponse getUserResponse() {
        return new UserResponse(1L, "tester", "test-user", "test@gmail.com", UserRole.USER);
    }


}