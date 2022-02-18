package com.windstoss.messanger.api.mapper;

import com.windstoss.messanger.api.dto.Message.GroupChatMessageRetrievalDto;
import com.windstoss.messanger.domain.Messages.GroupMessages.GroupChatTextMessage;

public class GroupChatTextMessageRetrievalDtoMapper {
    public static GroupChatMessageRetrievalDto map(GroupChatTextMessage message){
        return GroupChatMessageRetrievalDto.builder()
                .text(message.getContent())
                .messageId(message.getId())
                .build();
    }
}
