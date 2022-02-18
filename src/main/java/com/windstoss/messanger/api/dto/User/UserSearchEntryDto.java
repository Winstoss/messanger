package com.windstoss.messanger.api.dto.User;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UserSearchEntryDto {

    private UUID userId;

    private String avatarPath;

    private String nickname;

}
