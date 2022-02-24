package com.windstoss.messanger.api.mapper;

import com.windstoss.messanger.api.dto.Message.MessageRetrievalDto;
import com.windstoss.messanger.domain.Messages.GroupMessages.GroupChatTextMessage;

public class GroupChatTextMessageRetrievalDtoMapper {
    public static MessageRetrievalDto map(GroupChatTextMessage message){
        return MessageRetrievalDto.builder()
                .text(message.getContent())
                .messageId(message.getId())
                .build();
    }
}
