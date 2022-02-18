package com.windstoss.messanger.api.dto.Chats;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ChatRetrievalDto {

    private UUID chatId;
    private String type;
    private String chatImage;
    private String chatName;
//    private String lastMessageContent;
//    private String lastMessageTime;

}
