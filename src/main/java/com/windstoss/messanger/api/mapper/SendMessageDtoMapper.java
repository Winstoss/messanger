package com.windstoss.messanger.api.mapper;

import com.windstoss.messanger.api.dto.Message.SendTextMessageDto;
import com.windstoss.messanger.domain.Messages.PrivateMessages.PrivateChatTextMessage;
import com.windstoss.messanger.domain.User;

public class SendMessageDtoMapper {
    public static PrivateChatTextMessage map(SendTextMessageDto message, User author) {
        return PrivateChatTextMessage.builder()
                .content(message.getText())
                .author(author)
                .build();
    }

}
