package com.windstoss.messanger.api.mapper;

import com.windstoss.messanger.api.dto.Message.SendTextMessageDto;
import com.windstoss.messanger.domain.Chats.PrivateChat;
import com.windstoss.messanger.domain.Messages.PrivateMessages.PrivateChatTextMessage;
import com.windstoss.messanger.domain.User;

public class PrivateTextMessageDtoMapper {
    public static PrivateChatTextMessage map(User author, PrivateChat chat, SendTextMessageDto data) {
        return PrivateChatTextMessage.builder()
                .author(author)
                .chat(chat)
                .content(data.getText())
                .build();
    }
}
