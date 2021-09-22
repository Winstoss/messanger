package com.windstoss.messanger.api.dto.Message;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@Builder
public class GroupChatTextMessageRetrievalDto {

    private UUID id;
    private String text;
}
