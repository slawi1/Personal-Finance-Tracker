package com.spring_project.user.service;

import com.spring_project.security.AuthenticationData;
import com.spring_project.user.model.User;
import com.spring_project.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);
        return null;

     //return new AuthenticationData(user.getId(), username, user.getPassword(), user.getRole(), user.getActiveProfile());
    }
}
