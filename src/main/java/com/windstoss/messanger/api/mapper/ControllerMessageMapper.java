package com.windstoss.messanger.api.mapper;

import com.windstoss.messanger.api.dto.Message.SendMessageDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface ControllerMessageMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "author", target = "author")
    @Mapping(source = "chat", target = "chatId")
    @Mapping(source = "message", target = "text")
    @Mapping(source = "file", target = "file")
    SendMessageDto map(String message, MultipartFile file, String author, UUID chat);
}
