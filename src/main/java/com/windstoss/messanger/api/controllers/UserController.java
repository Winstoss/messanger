package com.windstoss.messanger.api.controllers;

import com.windstoss.messanger.api.dto.EditUserDataDto;
import com.windstoss.messanger.domain.User;
import com.windstoss.messanger.services.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;


@RestController
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = Objects.requireNonNull(userService);
    }

    @PostMapping("/registration")
    public void registrateUser(@RequestBody User user) {
        userService.registerUser(user);
    }

    @PostMapping("/edit")
    public void editUser(@RequestHeader ("username") String login,
            @RequestBody EditUserDataDto editingDataDto){


        userService.editUser(login, editingDataDto);
    }
}
