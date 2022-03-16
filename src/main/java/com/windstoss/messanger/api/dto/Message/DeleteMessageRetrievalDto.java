package com.windstoss.messanger.api.dto.Message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class DeleteMessageRetrievalDto {

    @JsonIgnore
    private UUID chatId;

    private UUID messageId;

    private boolean isDeleted;

}
