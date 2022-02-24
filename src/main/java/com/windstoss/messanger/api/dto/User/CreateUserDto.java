package com.windstoss.messanger.api.dto.User;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateUserDto {
    private String nickname;
    private String username;
    private String password;
    private String phoneNumber;
    private String bio;
}
