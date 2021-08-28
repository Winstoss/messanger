package com.windstoss.messanger.api.mapper;


import com.windstoss.messanger.api.dto.EditUserDataDto;
import com.windstoss.messanger.domain.User;
import com.windstoss.messanger.utils.StringUtils;

public class UserDtoMapper {

    public static User dtoToUserMapper(User user, EditUserDataDto editingDataDto) {
        return user.toBuilder()
                .username(StringUtils.defaultIfEmpty(editingDataDto.getUsername(), user.getUsername()))
                .password(StringUtils.defaultIfEmpty(editingDataDto.getPassword(), user.getPassword()))
                .nickname(StringUtils.defaultIfEmpty(editingDataDto.getNickname(), user.getNickname()))
                .avatarPath(StringUtils.defaultIfEmpty(editingDataDto.getAvatar(), user.getAvatarPath()))
                .phoneNumber((StringUtils.defaultIfEmpty(editingDataDto.getPhoneNumber(), user.getPhoneNumber())))
                .bio(StringUtils.defaultIfEmpty(editingDataDto.getDescription(), user.getBio()))
                .build();

    }
}