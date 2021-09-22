package com.windstoss.messanger.api.mapper;

import com.windstoss.messanger.api.dto.Message.GroupChatTextMessageRetrievalDto;
import com.windstoss.messanger.domain.Messages.GroupMessages.GroupChatTextMessage;

public class GroupChatTextMessageRetrievalDtoMapper {
    public static GroupChatTextMessageRetrievalDto map(GroupChatTextMessage message){
        return GroupChatTextMessageRetrievalDto.builder()
                .text(message.getContent())
                .id(message.getId())
                .build();
    }
}
