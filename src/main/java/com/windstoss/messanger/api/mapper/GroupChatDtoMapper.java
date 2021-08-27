package com.windstoss.messanger.api.mapper;

import com.windstoss.messanger.api.dto.CreateGroupChatDto;
import com.windstoss.messanger.domain.Chats.GroupChat;

import java.util.*;

public class GroupChatDtoMapper {

    public static GroupChat dtoToGroupChat(CreateGroupChatDto dto) {
        return GroupChat.builder()
                .description(dto.getDescription())
                .creator(dto.getCreator())
                .admins(Set.of(dto.getCreator()))
                .users(new HashSet<>(dto.getInvitedUsers()))
                .build();
    }

}
