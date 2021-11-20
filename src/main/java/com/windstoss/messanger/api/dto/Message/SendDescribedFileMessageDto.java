package com.windstoss.messanger.api.dto.Message;

import org.springframework.web.multipart.MultipartFile;

public class SendDescribedFileMessageDto {

    private String user;

    private String text;
    private MultipartFile file;

}
