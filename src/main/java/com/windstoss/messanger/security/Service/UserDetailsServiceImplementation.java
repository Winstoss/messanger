package com.windstoss.messanger.security.Service;

import com.windstoss.messanger.api.exception.exceptions.UserNotFoundException;
import com.windstoss.messanger.domain.User;
import com.windstoss.messanger.repositories.UserRepository;
import com.windstoss.messanger.security.UserPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;


public class UserDetailsServiceImplementation implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImplementation(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(UserNotFoundException::new);
        return new UserPrincipal(user);
    }
}
