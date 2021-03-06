package com.windstoss.messanger.api.dto.Message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import java.util.UUID;

@Data
@Builder
public class MessageRetrievalDto {

    private UUID messageId;
    private String nickname;
    private String file;
    private String text;
    @JsonIgnore
    private UUID chatId;

}
