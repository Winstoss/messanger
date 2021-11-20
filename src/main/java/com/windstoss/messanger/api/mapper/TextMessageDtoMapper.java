package com.windstoss.messanger.api.mapper;

import com.windstoss.messanger.api.dto.Message.SendMessageDto;
import com.windstoss.messanger.api.dto.Message.SendTextMessageDto;
import com.windstoss.messanger.domain.Chats.GroupChat;
import com.windstoss.messanger.domain.Chats.PrivateChat;
import com.windstoss.messanger.domain.Messages.GroupMessages.GroupChatDescribedFileMessage;
import com.windstoss.messanger.domain.Messages.GroupMessages.GroupChatFileMessage;
import com.windstoss.messanger.domain.Messages.GroupMessages.GroupChatTextMessage;
import com.windstoss.messanger.domain.Messages.PrivateMessages.PrivateChatDescribedFileMessage;
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

    public static PrivateChatDescribedFileMessage mapDF(User author, PrivateChat chat, String path, String description) {
        return PrivateChatDescribedFileMessage.builder()
                .author(author)
                .description(description)
                .chat(chat)
                .filePath(path)
                .build();
    }

    public static GroupChatFileMessage mapGF(User author, GroupChat chat, String path) {
        return GroupChatFileMessage.builder()
                .author(author)
                .chat(chat)
                .filePath(path)
                .build();
    }

    public static GroupChatDescribedFileMessage mapGDF(User author, GroupChat chat, String path, String description) {
        return GroupChatDescribedFileMessage.builder()
                .author(author)
                .description(description)
                .chat(chat)
                .filePath(path)
                .build();
    }


}
