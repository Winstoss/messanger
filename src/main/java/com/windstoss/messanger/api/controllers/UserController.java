package com.windstoss.messanger.api.controllers;

import com.windstoss.messanger.api.dto.User.CreateUserDto;
import com.windstoss.messanger.api.dto.User.EditUserDataDto;
import com.windstoss.messanger.api.dto.User.UserDataDto;
import com.windstoss.messanger.api.dto.User.UserRetrievalDto;
import com.windstoss.messanger.services.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RequestMapping("/user")
@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = Objects.requireNonNull(userService);
    }

    @PostMapping("/registration")
    public void registrateUser(@RequestBody CreateUserDto user) {

        userService.registerUser(user);
    }

    @GetMapping("/{username}")
    public UserRetrievalDto getUser(@RequestHeader("credentials") String credentials,
                                    @PathVariable("username") String username) {
        return userService.getUser(UserDataDto.builder()
                .credentials(credentials)
                .username(username)
                .build());
    }

    @PatchMapping("/edit")
    public UserRetrievalDto editUser(@RequestHeader("username") String login,
                                     @RequestBody EditUserDataDto editingDataDto) {

        return userService.editUser(login, editingDataDto);
    }

    @DeleteMapping("/delete")
    public void deleteUser(@RequestHeader("credentials") String credentials) {

        userService.deleteUser(credentials);
    }
}
