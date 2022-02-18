package com.windstoss.messanger.api.dto.Chats.GroupChat;

import lombok.Builder;
import lombok.Data;



import java.util.UUID;

@Data
@Builder
public class GroupChatRetrievalDto {

    private UUID chatId;

    private final String chatType = "group";

    private String chatImage;

    private String chatName;

}
