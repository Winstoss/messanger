package com.windstoss.messanger.api.dto.Authorization;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthorizationDto {
    private String username;
    private String password;
}
