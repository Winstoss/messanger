package com.windstoss.messanger.api.dto.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditUserDataDto {

    private String nickname;
    private String password;
    private String phoneNumber;
    private String bio;
    private String avatarPath;

}
