package com.windstoss.messanger.api.dto.User;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDataDto {
    String credentials;
    String username;
}
