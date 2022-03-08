package com.windstoss.messanger.api.config;

import com.windstoss.messanger.repositories.UserRepository;
import com.windstoss.messanger.security.Filter.AuthorizationFilter;
import com.windstoss.messanger.security.Service.TokenService;
import com.windstoss.messanger.security.Service.UserDetailsServiceImplementation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserRepository userRepository;
    private final TokenService tokenService;

    public WebSecurityConfig(UserRepository userRepository, TokenService tokenService) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                        .authorizeRequests()
                        .antMatchers("/", "/login","/user/registration" , "/files/**").permitAll()
                        .anyRequest().authenticated()
                    .and()
                        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                        .addFilterBefore(
                                authorizationFilter(tokenService, userRepository),
                                UsernamePasswordAuthenticationFilter.class
                        ).cors();
                    // TODO rework credentials header since jwt implemented
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AuthorizationFilter authorizationFilter(TokenService tokenService, UserRepository userRepository) {
        return new AuthorizationFilter(tokenService, userRepository);
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImplementation(userRepository);
    }

}