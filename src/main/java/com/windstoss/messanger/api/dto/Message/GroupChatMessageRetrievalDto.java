package com.windstoss.messanger.api.dto.Message;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.Objects;
import java.util.UUID;

@Data
@Builder
public class GroupChatMessageRetrievalDto {

    private UUID messageId;
    private String nickname;
    private String file;
    private String text;


}
