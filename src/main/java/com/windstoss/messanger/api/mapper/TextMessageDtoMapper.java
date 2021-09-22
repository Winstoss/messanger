package com.windstoss.messanger.api.mapper;

import com.windstoss.messanger.api.dto.Message.SendMessageDto;
import com.windstoss.messanger.api.dto.Message.SendTextMessageDto;
import com.windstoss.messanger.domain.Chats.GroupChat;
import com.windstoss.messanger.domain.Chats.PrivateChat;
import com.windstoss.messanger.domain.Messages.GroupMessages.GroupChatTextMessage;
import com.windstoss.messanger.domain.Messages.PrivateMessages.PrivateChatFileMessage;
import com.windstoss.messanger.domain.Messages.PrivateMessages.PrivateChatTextMessage;
import com.windstoss.messanger.domain.User;

public class TextMessageDtoMapper {
    public static PrivateChatTextMessage map(User author, PrivateChat chat, SendTextMessageDto data) {
        return PrivateChatTextMessage.builder()
                .author(author)
                .chat(chat)
                .content(data.getText())
                .build();
    }
    public static GroupChatTextMessage map(User author, GroupChat chat, SendTextMessageDto data) {
        return GroupChatTextMessage.builder()
                .author(author)
                .chat(chat)
                .content(data.getText())
                .build();
    }

    public static PrivateChatTextMessage map(User author, PrivateChat chat, SendMessageDto data) {
        return PrivateChatTextMessage.builder()
                .author(author)
                .chat(chat)
                .content(data.getText())
                .build();
        }

    public static PrivateChatFileMessage mapF(User author, PrivateChat chat, String path) {
        return PrivateChatFileMessage.builder()
                .author(author)
                .chat(chat)
                .filePath(path)
                .build();
    }

}
