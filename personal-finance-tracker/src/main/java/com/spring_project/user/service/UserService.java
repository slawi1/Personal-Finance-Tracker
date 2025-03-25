package com.spring_project.user.service;

import com.spring_project.category.service.CategoryService;
import com.spring_project.exception.DomainException;
import com.spring_project.exception.EmailAlreadyRegisteredException;
import com.spring_project.exception.PasswordsDoNotMatchException;
import com.spring_project.exception.UsernameAlreadyExistException;
import com.spring_project.notification.service.NotificationService;
import com.spring_project.security.AuthenticationData;
import com.spring_project.user.model.Role;
import com.spring_project.user.model.User;
import com.spring_project.user.repository.UserRepository;
import com.spring_project.web.dto.AddCashRequest;
import com.spring_project.web.dto.EditProfileRequest;
import com.spring_project.web.dto.RegisterRequest;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CategoryService categoryService;
    private final NotificationService notificationService;


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, CategoryService categoryService, NotificationService notificationService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.categoryService = categoryService;
        this.notificationService = notificationService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username).orElseThrow(() -> new DomainException("User not found"));

        return new AuthenticationData(user.getId(), username, user.getPassword(), user.getRole(), user.getActiveProfile());
    }

    @Transactional
    public void register(RegisterRequest registerRequest) {

        Optional<User> optionalUsername = userRepository.findByUsername((registerRequest.getUsername()));
        if (optionalUsername.isPresent()) {
            throw new UsernameAlreadyExistException("Username '%s' is already taken".formatted(registerRequest.getUsername()));
        }
        Optional<User> optionalEmail = userRepository.findByEmail((registerRequest.getEmail()));
        if (optionalEmail.isPresent()) {
            throw new EmailAlreadyRegisteredException("Email '%s' is already registered".formatted(registerRequest.getEmail()));
        }

        User user = userRepository.save(createUser(registerRequest));

        categoryService.addDefaultCategories(user);

//        notificationService.savePreference(user.getId(), true, user.getEmail());

        log.info("User with id '%s' created successfully.".formatted(user.getId()));
    }

    public User createUser(RegisterRequest registerRequest) {

        if (registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
            User user = User.builder()
                    .username(registerRequest.getUsername())
                    .email(registerRequest.getEmail())
                    .password(passwordEncoder.encode(registerRequest.getPassword()))
                    .activeProfile(true)
                    .role(Role.USER)
                    .balance(BigDecimal.ZERO)
                    .build();

            if (userRepository.findAll().isEmpty()) {
                user.setRole(Role.ADMIN);
            }

            return user;
        }

        throw new PasswordsDoNotMatchException("Passwords do not match");
    }


    public void addCash(AddCashRequest addCashRequest, UUID userId) {

//        User user = getById(userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new DomainException("User not found"));

        user.setBalance(user.getBalance().add(addCashRequest.getAmount()));
        userRepository.save(user);
    }

    public void subtractCash(BigDecimal expense, UUID userId) {

        User user = getById(userId);
        user.setBalance(user.getBalance().subtract(expense));
        userRepository.save(user);
    }

    public User getById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new DomainException("User not found"));
    }


    public void editProfile(UUID id, EditProfileRequest editProfileRequest) {

//        User user = getById(id);
        User user = userRepository.findById(id).orElseThrow(() -> new DomainException("User not found"));

        user.setFirstName(editProfileRequest.getFirstName());
        user.setLastName(editProfileRequest.getLastName());
        user.setProfilePicture(editProfileRequest.getProfilePicture());

        userRepository.save(user);
    }


}
