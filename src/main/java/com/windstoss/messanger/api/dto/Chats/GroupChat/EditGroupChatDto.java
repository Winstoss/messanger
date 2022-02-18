package com.windstoss.messanger.api.dto.Chats.GroupChat;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class EditGroupChatDto {

    private String title;
    private MultipartFile image;

}
