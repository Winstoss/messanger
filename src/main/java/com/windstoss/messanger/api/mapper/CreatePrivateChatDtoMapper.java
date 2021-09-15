package com.windstoss.messanger.api.mapper;

import com.windstoss.messanger.api.dto.PrivateChat.PrivateChatDto;

public class CreatePrivateChatDtoMapper {
    public static String dtoToUser(PrivateChatDto data) {
        return data.getUsername();
    }
}
