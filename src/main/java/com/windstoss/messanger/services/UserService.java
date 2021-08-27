package com.windstoss.messanger.services;

import com.windstoss.messanger.api.dto.EditUserDataDto;
import com.windstoss.messanger.api.mapper.UserDtoMapper;
import com.windstoss.messanger.domain.User;
import com.windstoss.messanger.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;


@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = Objects.requireNonNull(userRepository);
    }

    public void registerUser(User user) {
        userRepository.findByUsername(user.getUsername())
                .ifPresent(usr -> {
                    throw new IllegalArgumentException();
                });

        userRepository.save(user);
    }

    public User editUser(String login,
                         EditUserDataDto editingDataDto) {

        User user = userRepository.findByUsername(login)
                .orElseThrow(() -> new IllegalArgumentException());

        return userRepository.save(UserDtoMapper.dtoToUserMapper(user, editingDataDto));
    }
}
