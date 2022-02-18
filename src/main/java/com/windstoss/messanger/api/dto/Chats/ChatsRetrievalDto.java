package com.windstoss.messanger.api.dto.Chats;

import lombok.Data;

import java.util.List;

@Data
public class ChatsRetrievalDto {
    private List<ChatRetrievalDto> chatList;
}
