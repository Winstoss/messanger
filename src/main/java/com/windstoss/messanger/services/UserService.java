package com.windstoss.messanger.services;

import com.windstoss.messanger.api.dto.User.CreateUserDto;
import com.windstoss.messanger.api.dto.User.EditUserDataDto;
import com.windstoss.messanger.api.mapper.CreateUserDtoMapper;
import com.windstoss.messanger.api.mapper.EditUserDtoMapper;
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

    public void registerUser(CreateUserDto userData) {

        User user = CreateUserDtoMapper.dtoToUser(userData);

        userRepository.findUserByUsername(user.getUsername())
                .ifPresent(usr -> {
                    throw new IllegalArgumentException();
                });

        userRepository.save(user);
    }

    public User editUser(String login,
                         EditUserDataDto editingDataDto) {

        User user = userRepository.findUserByUsername(login)
                .orElseThrow(IllegalArgumentException::new);

        return userRepository.save(EditUserDtoMapper.dtoToUserMapper(user, editingDataDto));
    }

    public void deleteUser(String login) {
        User user = userRepository.findUserByUsername(login)
                .orElseThrow(IllegalArgumentException::new);

        userRepository.delete(user);
    }
}
