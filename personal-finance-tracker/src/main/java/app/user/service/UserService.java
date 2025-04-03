package app.user.service;

import app.category.service.CategoryService;
import app.event.UserRegisteredEventProducer;
import app.event.payload.UserRegisteredEvent;
import app.exception.DomainException;
import app.exception.EmailAlreadyRegisteredException;
import app.exception.PasswordsDoNotMatchException;
import app.exception.UsernameAlreadyExistException;
import app.notification.service.NotificationService;
import app.security.AuthenticationData;
import app.user.model.Role;
import app.user.model.User;
import app.user.repository.UserRepository;
import app.web.dto.AddCashRequest;
import app.web.dto.EditProfileRequest;
import app.web.dto.RegisterRequest;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CategoryService categoryService;
    private final NotificationService notificationService;
    private final UserRegisteredEventProducer userRegisteredEventProducer;


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, CategoryService categoryService, NotificationService notificationService, UserRegisteredEventProducer userRegisteredEventProducer) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.categoryService = categoryService;
        this.notificationService = notificationService;
        this.userRegisteredEventProducer = userRegisteredEventProducer;
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
            throw new UsernameAlreadyExistException("Username '%s' is already taken!".formatted(registerRequest.getUsername()));
        }
        Optional<User> optionalEmail = userRepository.findByEmail((registerRequest.getEmail()));
        if (optionalEmail.isPresent()) {
            throw new EmailAlreadyRegisteredException("Email '%s' is already registered!".formatted(registerRequest.getEmail()));
        }

        User user = userRepository.save(createUser(registerRequest));

        categoryService.addDefaultCategories(user);

        UserRegisteredEvent event = UserRegisteredEvent.builder()
                .id(user.getId())
                .username(user.getUsername())
                .registeredAt(LocalDateTime.now())
                .build();
        userRegisteredEventProducer.send(event);
        notificationService.savePreference(user.getId(), true, user.getEmail());

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

        throw new PasswordsDoNotMatchException("Passwords do not match!");
    }


    public void addCash(AddCashRequest addCashRequest, UUID userId) {

        User user = getById(userId);
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

        User user = getById(id);
        user.setFirstName(editProfileRequest.getFirstName());
        user.setLastName(editProfileRequest.getLastName());
        user.setProfilePicture(editProfileRequest.getProfilePicture());

        userRepository.save(user);
    }

    public List<User> allUsers() {
        return userRepository.findAll();
    }

    public void switchStatus(UUID id) {
        User user = getById(id);
        user.setActiveProfile(!user.getActiveProfile());
        userRepository.save(user);
    }

    public void changeRole(UUID id) {
        User user = getById(id);
        if (user.getRole() == Role.ADMIN) {
            user.setRole(Role.USER);
            userRepository.save(user);
            return;
        }
        user.setRole(Role.ADMIN);
        userRepository.save(user);
    }

}
