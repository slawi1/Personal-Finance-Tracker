package com.spring_project.personal_finance_tracker.user;

import com.spring_project.category.service.CategoryService;
import com.spring_project.exception.DomainException;
import com.spring_project.exception.EmailAlreadyRegisteredException;
import com.spring_project.exception.UsernameAlreadyExistException;
import com.spring_project.notification.service.NotificationService;
import com.spring_project.user.model.User;
import com.spring_project.user.repository.UserRepository;
import com.spring_project.user.service.UserService;
import com.spring_project.web.dto.AddCashRequest;
import com.spring_project.web.dto.EditProfileRequest;
import com.spring_project.web.dto.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceUTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private CategoryService categoryService;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private UserService userService;


    @Test
    void givenUserWithExistingUsername_thenRegisterThrowsException() {

        User user = User.builder()
                .id(UUID.randomUUID())
                .username("username")
                .build();

        RegisterRequest registerRequest = RegisterRequest.builder()
                .username("username")
                .build();

        when(userRepository.findByUsername(registerRequest.getUsername())).thenReturn(Optional.of(user));

        assertThrows(UsernameAlreadyExistException.class, () -> userService.register(registerRequest));
    }
    @Test
    void givenUserWithExistingEmail_thenRegisterThrowsException() {

        User user = User.builder()
                .id(UUID.randomUUID())
                .email("email@abv.bg")
                .build();

        RegisterRequest registerRequest = RegisterRequest.builder()
                .email("email@abv.bg")
                .build();

        when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.of(user));

        assertThrows(EmailAlreadyRegisteredException.class, () -> userService.register(registerRequest));
    }


    @Test
    void givenUserAndAddCashRequest_thenAddCashToUser_shouldUpdateUserBalance() {

        UUID uuid = UUID.randomUUID();

        User user = User.builder()
                .id(uuid)
                .balance(BigDecimal.ZERO)
                .build();

        AddCashRequest addCashRequest = new AddCashRequest();
        addCashRequest.setAmount(BigDecimal.ONE);

        when(userRepository.findById(uuid)).thenReturn(Optional.of(user));

        userService.addCash(addCashRequest, user.getId());

        assertEquals(BigDecimal.ONE, user.getBalance());
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void givenUserAndAddExpense_thenSubtractCash_shouldUpdateUserBalance() {

        UUID uuid = UUID.randomUUID();

        User user = User.builder()
                .id(uuid)
                .balance(BigDecimal.valueOf(10))
                .build();
        BigDecimal expense = BigDecimal.valueOf(5);
        when(userRepository.findById(uuid)).thenReturn(Optional.of(user));

        userService.subtractCash(expense, user.getId());

        assertEquals(BigDecimal.valueOf(5), user.getBalance());
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void givenNonExistentUser_thenThrowDomainException() {
        UUID uuid = UUID.randomUUID();

        when(userRepository.findById(uuid)).thenReturn(Optional.empty());

        assertThrows(DomainException.class , () -> userService.getById(uuid));

    }
    @Test
    void givenExistingUser_thenReturnUser() {
        UUID uuid = UUID.randomUUID();

        User user = new User();
        when(userRepository.findById(uuid)).thenReturn(Optional.of(user));

        userService.getById(uuid);

        assertThat(userService.getById(uuid)).isEqualTo(user);

    }

    @Test
    void givenUserIdAndEditProfileRequest_thenSuccessfullyUpdateProfile() {
        UUID uuid = UUID.randomUUID();
        User user = User.builder()
                .id(uuid)
                .build();
        EditProfileRequest editProfileRequest = EditProfileRequest.builder()
                .firstName("first")
                .lastName("last")
                .profilePicture("www.url.com")
                .build();

        when(userRepository.findById(uuid)).thenReturn(Optional.of(user));

        userService.editProfile(user.getId(), editProfileRequest);

        assertEquals(editProfileRequest.getFirstName(), user.getFirstName());
        assertEquals(editProfileRequest.getLastName(), user.getLastName());
        assertEquals(editProfileRequest.getProfilePicture(), user.getProfilePicture());
        verify(userRepository, times(1)).save(any());
    }



}
