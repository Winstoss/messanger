package com.windstoss.messanger.api.controllers;

import com.windstoss.messanger.api.dto.User.*;
import com.windstoss.messanger.domain.User;
import com.windstoss.messanger.services.UserService;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@CrossOrigin
@RequestMapping("/user")
@RestController()
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = Objects.requireNonNull(userService);
    }

    @PostMapping("/registration")
    public boolean registrateUser(@RequestBody CreateUserDto user) {

       return userService.registerUser(user);
    }

    @GetMapping("/me")
    public UserRetrievalDto getCurrentUser( UsernamePasswordAuthenticationToken principal)
    {
        return userService.getCurrentUser((User) principal.getPrincipal());
    }

    @GetMapping("/find")
    public List<UserSearchEntryDto> getUser(@RequestParam(name = "q") String username,
                                            UsernamePasswordAuthenticationToken principal)
    {   final User requester =  (User) principal.getPrincipal();
        return userService.getUser(username, requester);
    }

    @PatchMapping(value = "/me", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UserRetrievalDto editUser( @RequestParam(required = false) String nickname,
                                      @RequestParam(required = false) String bio,
                                      @RequestParam(required = false) String phoneNumber,
                                      @RequestParam(required = false) String password,
                                      @RequestParam(required = false) MultipartFile image,
                                      UsernamePasswordAuthenticationToken principal) throws IOException {


        return userService.editUser((User) principal.getPrincipal(),
                EditUserDataWithFileDto.builder()
                        .avatarPath(image)
                        .bio(bio)
                        .phoneNumber(phoneNumber)
                        .nickname(nickname)
                        .password(password)
                        .build());
    }

    @DeleteMapping("/delete")
    public void deleteUser(UsernamePasswordAuthenticationToken principal) {

        userService.deleteUser((User) principal.getPrincipal());
    }
}
