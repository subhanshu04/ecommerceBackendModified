package com.example.ecommerce.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class WebSecurityConfig {
    private JWTRequestFilter jwtRequestFilter;

    public WebSecurityConfig(JWTRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    //It blocks the request of client to the mentioned endpoints. If we permitAll then all the endpoint/apis can be accessed without login.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //If csrf and cors are not disable then it wont allow request matcher for auth/login

        //http.csrf().disable().cors().disable();  // This is depreciated, so we use below one.

        http.cors(AbstractHttpConfigurer::disable);
        http.csrf(AbstractHttpConfigurer::disable);

        http.addFilterBefore(jwtRequestFilter, AuthorizationFilter.class);

        //Hello Mr http, authorize all the request.
        // This is depreciated, so we use the one after this.
//        http.authorizeHttpRequests().
//                requestMatchers("/auth/login","/product/**").permitAll().
//                anyRequest().authenticated();

        //hello mr http only authorized req which matches the provided req and for rest of the request check if it is authenticated or not.

        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/auth/login","/auth/register","/product/**","/auth/verify**",
                                "/auth/requestLink","/auth/resetPassword").permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}
