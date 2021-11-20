package com.windstoss.messanger.api.mapper;

import com.windstoss.messanger.api.dto.Message.MessageContentDto;
import com.windstoss.messanger.api.dto.Message.MessageRetrievalDto;
import com.windstoss.messanger.api.dto.Message.SendMessageDto;
import com.windstoss.messanger.domain.Chats.GroupChat;
import com.windstoss.messanger.domain.Chats.PrivateChat;
import com.windstoss.messanger.domain.Messages.GroupMessages.GroupChatFileMessage;
import com.windstoss.messanger.domain.Messages.GroupMessages.GroupChatMessageSignature;
import com.windstoss.messanger.domain.Messages.GroupMessages.GroupChatTextMessage;
import com.windstoss.messanger.domain.Messages.PrivateMessages.PrivateChatFileMessage;
import com.windstoss.messanger.domain.Messages.PrivateMessages.PrivateChatMessageSignature;
import com.windstoss.messanger.domain.Messages.PrivateMessages.PrivateChatTextMessage;
import com.windstoss.messanger.domain.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "signature.id", target = "id")
    @Mapping(source = "author", target = "author")
    @Mapping(source = "chat", target = "chat")
    @Mapping(source = "message", target = "content")
    PrivateChatTextMessage map(String message, User author, PrivateChat chat, PrivateChatMessageSignature signature);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "signature.id", target = "id")
    @Mapping(source = "author", target = "author")
    @Mapping(source = "chat", target = "chat")
    @Mapping(target = "filePath", expression = "java(message.getAbsolutePath())")
    PrivateChatFileMessage map(File message, User author, PrivateChat chat, PrivateChatMessageSignature signature);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "signature.id", target = "id")
    @Mapping(source = "author", target = "author")
    @Mapping(source = "chat", target = "chat")
    @Mapping(source = "message", target = "content")
    GroupChatTextMessage map(String message, User author, GroupChat chat, GroupChatMessageSignature signature);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "signature.id", target = "id")
    @Mapping(source = "author", target = "author")
    @Mapping(source = "chat", target = "chat")
    @Mapping(target = "filePath", expression = "java(message.getAbsolutePath())")
    GroupChatFileMessage map(File message, User author, GroupChat chat, GroupChatMessageSignature signature);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "signature.id", target = "id")
    @Mapping(target = "author", expression = "java(signature.getAuthor().getUsername())")
    @Mapping(source = "textMessage.content", target = "message")
    @Mapping(source = "fileMessage.filePath", target = "filePath")
    MessageRetrievalDto map(GroupChatMessageSignature signature,
                            GroupChatTextMessage textMessage,
                            GroupChatFileMessage fileMessage);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "signature.id", target = "id")
    @Mapping(target = "author", expression = "java(signature.getAuthor().getUsername())")
    @Mapping(source = "textMessage.content", target = "message")
    @Mapping(source = "fileMessage.filePath", target = "filePath")
    MessageRetrievalDto map(PrivateChatMessageSignature signature,
                            PrivateChatTextMessage textMessage,
                            PrivateChatFileMessage fileMessage);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "dto.text", target = "text")
    @Mapping(source = "dto.file", target = "file")
    MessageContentDto map(SendMessageDto dto);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "author", target = "author")
    @Mapping(source = "privateChat", target = "chat")
    PrivateChatMessageSignature map(User author, PrivateChat privateChat);
}
