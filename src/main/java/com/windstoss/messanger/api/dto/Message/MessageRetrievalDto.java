package com.windstoss.messanger.api.dto.Message;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
public class MessageRetrievalDto {
    UUID id;
    String author;
    String message;
    String filePath;
}
