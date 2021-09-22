package com.windstoss.messanger.api.dto.User;

import lombok.Data;

@Data
public class UserRetrievalDto {

    private String username;

    private String nickname;

    private String bio;

    private String avatarPath;

    private String phoneNumber;

}
