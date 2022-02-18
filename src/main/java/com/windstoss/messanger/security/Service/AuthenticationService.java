package com.windstoss.messanger.security.Service;

import com.windstoss.messanger.api.dto.Authorization.AuthorizationDto;
import com.windstoss.messanger.api.dto.Authorization.AuthorizationRetrievalDto;
import com.windstoss.messanger.api.exception.exceptions.InvalidCredentialsException;
import com.windstoss.messanger.api.exception.exceptions.UserAbsenceException;
import com.windstoss.messanger.domain.User;
import com.windstoss.messanger.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthenticationService {

    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final PasswordEncoder enc;

    public AuthenticationService(TokenService tokenService, UserRepository userRepository, PasswordEncoder enc) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
        this.enc = enc;
    }

    @Transactional
    public AuthorizationRetrievalDto login(AuthorizationDto data) {
        final User user = userRepository.findUserByUsername(data.getUsername()).orElseThrow(UserAbsenceException::new);
        if (!enc.matches(data.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }
        return AuthorizationRetrievalDto.builder()
                .token(tokenService.generateToken(data.getUsername()))
                .userId(user.getId())
                .bio(user.getBio())
                .nickname(user.getNickname())
                .avatarPath(user.getAvatarPath())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }


}
