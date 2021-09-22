package com.windstoss.messanger.api.dto.Message;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;



@Data
public class SendMessageDto {
    private String text;
    private MultipartFile file;
    private String author;
    private UUID chatId;
}
