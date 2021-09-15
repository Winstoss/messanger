package com.windstoss.messanger.api.dto.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditUserDataDto {
    private String nickname;
    private String username;
    private String password;
    private String phone_number;
    private String bio;
    private String avatar_path;
}
