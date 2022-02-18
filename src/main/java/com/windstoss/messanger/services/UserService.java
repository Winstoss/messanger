package com.windstoss.messanger.services;

import com.windstoss.messanger.api.dto.User.*;
import com.windstoss.messanger.api.exception.exceptions.UserCreationException;
import com.windstoss.messanger.api.exception.exceptions.UserNotFoundException;
import com.windstoss.messanger.api.mapper.TextMessageDtoMapper;
import com.windstoss.messanger.api.mapper.UserMapper;
import com.windstoss.messanger.domain.User;
import com.windstoss.messanger.repositories.UserRepository;
import com.windstoss.messanger.security.Service.TokenService;
import com.windstoss.messanger.utils.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@Transactional
public class UserService {

    private String uploadPath;

    private final UserMapper userMapper;

    private final UserRepository userRepository;

    public UserService(@Value("${upload.path}") String uploadPath,
                       UserRepository userRepository,
                       UserMapper userMapper) {
        this.uploadPath = uploadPath;
        this.userRepository = Objects.requireNonNull(userRepository);
        this.userMapper = Objects.requireNonNull(userMapper);
    }

    public UserRetrievalDto getCurrentUser(User user){

        return userMapper.map(user);
    }

    public List<UserSearchEntryDto> getUser(String username, User user){

        List<User> foundUsers = userRepository.userSearch(username);

        return foundUsers.stream()
                .filter(it -> !(it.equals(user)))
                .map(it -> UserSearchEntryDto.builder()
                        .nickname(it.getNickname())
                        .userId(it.getId())
                        .avatarPath(it.getAvatarPath())
                        .build())
                .collect(Collectors.toList());

    }



    public void registerUser(CreateUserDto userData) {

        User user = userMapper.map(userData);

        userRepository.findUserByUsername(user.getUsername())
                .ifPresent(usr -> {
                    throw new UserCreationException();
                });

        userRepository.save(user);
    }

    public UserRetrievalDto editUser(User user, EditUserDataWithFileDto editingDataDto) throws IOException {

        String fileName = null;
        if (!(editingDataDto.getAvatarPath() == null)){

            if (!StringUtils.isEmpty(user.getAvatarPath())){
                user.setAvatarPath("");
            }

            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String fileId = UUID.randomUUID().toString();
            fileName = fileId + "." + editingDataDto.getAvatarPath().getOriginalFilename();
            String filePath = uploadPath + "\\" + fileName;

            editingDataDto.getAvatarPath().transferTo(new File(filePath));


        }


        User edited = userMapper.map(EditUserDataDto.builder()
                        .avatarPath(fileName)
                        .bio(editingDataDto.getBio())
                        .nickname(editingDataDto.getNickname())
                        .password(editingDataDto.getPassword())
                        .phoneNumber(editingDataDto.getPhoneNumber())
                        .build());

        return userMapper.map(userRepository.save(user.merge(edited)));
    }

    public void deleteUser(User toDelete) {

        userRepository.delete(toDelete);
    }
}
