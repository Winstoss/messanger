package com.windstoss.messanger.security.Filter;

import com.windstoss.messanger.api.exception.exceptions.UserAbsenceException;
import com.windstoss.messanger.domain.User;
import com.windstoss.messanger.repositories.UserRepository;
import com.windstoss.messanger.security.Service.TokenService;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

import static com.windstoss.messanger.security.Service.TokenService.JWT_TOKEN_PREFIX;


public class AuthorizationFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UserRepository userRepository;

    public AuthorizationFilter(TokenService tokenService, UserRepository userRepository) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain
    ) throws ServletException, IOException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith(JWT_TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.replace(JWT_TOKEN_PREFIX, "");

        try {
            String username = tokenService.getUsernameFromToken(token);
            User user = userRepository.findUserByUsername(username).orElseThrow(UserAbsenceException::new);
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                    user, user.getPassword(), Collections.EMPTY_LIST
            ));

        } catch (UsernameNotFoundException | SignatureException e) {
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }


}
