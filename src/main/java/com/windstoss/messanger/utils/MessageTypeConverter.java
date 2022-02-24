package com.windstoss.messanger.utils;

import com.windstoss.messanger.api.dto.Message.MessageTypes;
import org.springframework.core.convert.converter.Converter;

public class MessageTypeConverter implements Converter<String, MessageTypes> {

    @Override
    public MessageTypes convert(String source) {
        return MessageTypes.valueOf(source.toUpperCase());
    }
}
