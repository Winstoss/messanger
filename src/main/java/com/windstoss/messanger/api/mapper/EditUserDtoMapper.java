package com.windstoss.messanger.api.mapper;


import com.windstoss.messanger.api.dto.User.EditUserDataDto;
import com.windstoss.messanger.domain.User;
import com.windstoss.messanger.utils.StringUtils;

public class EditUserDtoMapper {

    public static User dtoToUserMapper(User user, EditUserDataDto editingDataDto) {
        return user.toBuilder()
                .username(StringUtils.defaultIfEmpty(editingDataDto.getUsername(), user.getUsername()))
                .password(StringUtils.defaultIfEmpty(editingDataDto.getPassword(), user.getPassword()))
                .nickname(StringUtils.defaultIfEmpty(editingDataDto.getNickname(), user.getNickname()))
                .avatar_path(StringUtils.defaultIfEmpty(editingDataDto.getAvatar_path(), user.getAvatar_path()))
                .phone_number((StringUtils.defaultIfEmpty(editingDataDto.getPhone_number(), user.getPhone_number())))
                .bio(StringUtils.defaultIfEmpty(editingDataDto.getBio(), user.getBio()))
                .build();

    }
}