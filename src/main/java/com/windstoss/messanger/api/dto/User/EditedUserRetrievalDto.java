package com.windstoss.messanger.api.dto.User;

import lombok.Data;

@Data
public class EditedUserRetrievalDto {

    private String nickname;

    private String bio;

    private String avatarPath;

    private String phoneNumber;
}
