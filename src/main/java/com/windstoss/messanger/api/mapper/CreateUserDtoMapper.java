package com.windstoss.messanger.api.mapper;

import com.windstoss.messanger.api.dto.User.CreateUserDto;
import com.windstoss.messanger.domain.User;
import com.windstoss.messanger.utils.StringUtils;

public class CreateUserDtoMapper {
    public static User dtoToUser(CreateUserDto data){
        return User.builder()
                .nickname(data.getNickname())
                .username(data.getUsername())
                .password(data.getPassword())
                .bio(StringUtils.defaultIfEmpty(data.getBio(), ""))
                .phone_number(StringUtils.defaultIfEmpty(data.getPhone_number(), ""))
                .avatar_path("")
                .build();
    }
}
