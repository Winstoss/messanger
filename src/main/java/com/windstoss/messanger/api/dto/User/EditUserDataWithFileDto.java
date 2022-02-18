package com.windstoss.messanger.api.dto.User;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class EditUserDataWithFileDto {

    private String nickname;
    private String password;
    private String phoneNumber;
    private String bio;
    private MultipartFile avatarPath;

}
