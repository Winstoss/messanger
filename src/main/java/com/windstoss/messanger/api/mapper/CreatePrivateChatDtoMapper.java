package com.windstoss.messanger.api.mapper;

import com.windstoss.messanger.api.dto.Chats.PrivateChat.PrivateChatDto;

public class CreatePrivateChatDtoMapper {
    public static String dtoToUser(PrivateChatDto data) {
        return data.getUsername();
    }
}
