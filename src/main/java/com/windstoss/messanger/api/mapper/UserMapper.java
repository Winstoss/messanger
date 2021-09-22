package com.windstoss.messanger.api.mapper;

import com.windstoss.messanger.api.dto.User.CreateUserDto;
import com.windstoss.messanger.api.dto.User.EditUserDataDto;
import com.windstoss.messanger.api.dto.User.UserRetrievalDto;
import com.windstoss.messanger.domain.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User map(CreateUserDto dto);

    User map(EditUserDataDto dto);

    UserRetrievalDto map(User user);
}
