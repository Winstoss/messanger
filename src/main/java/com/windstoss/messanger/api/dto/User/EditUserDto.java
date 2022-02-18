package com.windstoss.messanger.api.dto.User;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EditUserDto {

    private String nickname;
    private String password;
    private String phoneNumber;
    private String bio;

}
