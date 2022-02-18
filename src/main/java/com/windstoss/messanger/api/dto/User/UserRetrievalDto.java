package com.windstoss.messanger.api.dto.User;

import lombok.Data;

import java.util.UUID;

@Data
public class UserRetrievalDto {

    private String nickname;

    private String bio;

    private String avatarPath;

    private String phoneNumber;

}
