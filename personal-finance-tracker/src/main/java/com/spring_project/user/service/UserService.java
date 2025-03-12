package com.spring_project.user.service;

import com.spring_project.exception.DomainException;
import com.spring_project.exception.UsernameAlreadyExistException;
import com.spring_project.security.AuthenticationData;
import com.spring_project.user.model.Role;
import com.spring_project.user.model.User;
import com.spring_project.user.repository.UserRepository;
import com.spring_project.web.dto.RegisterRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username).orElseThrow(() -> new DomainException("User not found"));

     return new AuthenticationData(user.getId(),username,user.getPassword(),user.getRole(), user.getActiveProfile());
    }

    public void register(RegisterRequest registerRequest) {

        Optional<User> optionalUser = userRepository.findByUsername((registerRequest.getUsername()));
        if (optionalUser.isPresent()) {
            throw new UsernameAlreadyExistException("Username '%s' already in use".formatted(registerRequest.getUsername()));
        }

        User user = userRepository.save(createUser(registerRequest));

        log.info("User with id '%s' created successfully.".formatted(user.getId()));
    }

    public User createUser(RegisterRequest registerRequest) {

        return User.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .activeProfile(true)
                .role(Role.USER)
                .build();
    }

    public User getById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new DomainException("User not found"));
    }
}
