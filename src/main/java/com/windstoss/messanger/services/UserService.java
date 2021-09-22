package com.windstoss.messanger.services;

import com.windstoss.messanger.api.dto.User.CreateUserDto;
import com.windstoss.messanger.api.dto.User.EditUserDataDto;
import com.windstoss.messanger.api.dto.User.UserDataDto;
import com.windstoss.messanger.api.dto.User.UserRetrievalDto;
import com.windstoss.messanger.api.mapper.UserMapper;
import com.windstoss.messanger.domain.User;
import com.windstoss.messanger.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;


@Service
@Transactional
public class UserService {

    private final UserMapper userMapper;

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository,
                       UserMapper userMapper) {
        this.userRepository = Objects.requireNonNull(userRepository);
        this.userMapper = Objects.requireNonNull(userMapper);
    }

    public UserRetrievalDto getUser(UserDataDto data){

        userRepository.findUserByUsername(data.getCredentials())
                .orElseThrow(IllegalArgumentException::new);

        return userMapper.map(userRepository.findUserByUsername(data.getUsername())
                .orElseThrow(IllegalArgumentException::new));
    }

    public void registerUser(CreateUserDto userData) {

        User user = userMapper.map(userData);

        userRepository.findUserByUsername(user.getUsername())
                .ifPresent(usr -> {
                    throw new IllegalArgumentException();
                });

        userRepository.save(user);
    }

    public UserRetrievalDto editUser(String login,
                                     EditUserDataDto editingDataDto) {

        User user = userRepository.findUserByUsername(login)
                .orElseThrow(IllegalArgumentException::new);

        User edited = userMapper.map(editingDataDto);


        return userMapper.map(userRepository.save(user.merge(edited)));
    }

    public void deleteUser(String login) {
        User user = userRepository.findUserByUsername(login)
                .orElseThrow(IllegalArgumentException::new);

        userRepository.delete(user);
    }
}
