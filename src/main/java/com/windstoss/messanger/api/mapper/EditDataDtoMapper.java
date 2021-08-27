package com.windstoss.messanger.api.mapper;

import com.windstoss.messanger.api.dto.EditGroupChatDto;
import com.windstoss.messanger.domain.Chats.GroupChat;
import com.windstoss.messanger.domain.User;

import java.util.Set;

public class EditDataDtoMapper {

    public static GroupChat editDataDtoToGroupChat(EditGroupChatDto editData, Set<User> users, Set<User> admins) {
        return GroupChat.builder()
                .description(editData.getDescription())
                .users(users)
                .admins(admins)
                .build();
    }
}
