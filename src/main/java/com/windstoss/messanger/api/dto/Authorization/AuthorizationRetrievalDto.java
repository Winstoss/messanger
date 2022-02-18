package com.windstoss.messanger.api.dto.Authorization;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class AuthorizationRetrievalDto {

    private String token;
    private UUID userId;
    private String avatarPath;
    private String nickname;
    private String bio;
    private String phoneNumber;

}
