package com.windstoss.messanger.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditUserDataDto {
    private String username;
    private String nickname;
    private String phoneNumber;
    private String password;
    private String description;
    private String avatar;
}
