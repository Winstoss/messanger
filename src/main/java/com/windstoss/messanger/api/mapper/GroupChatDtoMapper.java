package com.windstoss.messanger.api.mapper;

import com.windstoss.messanger.api.dto.Chats.GroupChat.CreateGroupChatDto;
import com.windstoss.messanger.domain.Chats.GroupChat;
import com.windstoss.messanger.domain.User;
import com.windstoss.messanger.utils.StringUtils;


import java.util.*;

public class GroupChatDtoMapper {

    public static GroupChat dtoToGroupChat(CreateGroupChatDto dto, User creator) {
        return GroupChat.builder()
                .title(StringUtils.defaultIfEmpty(dto.getTitle(), ""))
                .creator(creator)
                .admins(new HashSet<>(Collections.singletonList(creator)))
                .users(new HashSet<>(Collections.singletonList(creator)))
                .imagePath(StringUtils.defaultIfEmpty(dto.getImagePath(), ""))
                .build();
    }

}
