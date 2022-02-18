package com.windstoss.messanger.api.controllers;

import com.windstoss.messanger.api.dto.Authorization.AuthorizationDto;
import com.windstoss.messanger.api.dto.Authorization.AuthorizationRetrievalDto;
import com.windstoss.messanger.api.exception.exceptions.InvalidCredentialsException;
import com.windstoss.messanger.security.Service.AuthenticationService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@CrossOrigin
public class AuthorizationController {

    private final AuthenticationService authenticationService;

    public AuthorizationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public AuthorizationRetrievalDto login(@RequestBody AuthorizationDto credentials) {
        try {
            return authenticationService.login(credentials);
        } catch (Exception e) {
            throw new InvalidCredentialsException(e);
        }

    }
}
