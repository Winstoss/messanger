package com.windstoss.messanger.api.dto.GroupChat;

import lombok.Builder;
import lombok.Data;



import java.util.UUID;

@Data
@Builder
public class GroupChatRetrievalDto {

    private UUID id;

    private String imagePath;

    private String title;

    //TODO : add zoned creation time
}
