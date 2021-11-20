package com.windstoss.messanger.api.dto.Message;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class MessageContentDto {
    private String text;
    private MultipartFile file;
}
