package com.windstoss.messanger.api.dto.Chats.PrivateChat;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class PrivateChatRetrievalDto {

    private UUID chatId;
    private final String chatType = "private";
    private String chatImage;
    private String chatName;

}
