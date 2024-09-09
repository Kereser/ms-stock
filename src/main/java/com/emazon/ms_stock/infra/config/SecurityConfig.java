package com.emazon.ms_stock.infra.config;

import com.emazon.ms_stock.infra.security.entrypoint.CustomJWTEntryPoint;
import com.emazon.ms_stock.infra.security.filter.JwtValidatorFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CustomJWTEntryPoint jwtEntryPoint) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> {
                auth.requestMatchers(HttpMethod.POST, "/stock/**").hasRole("ADMIN");
                auth.requestMatchers(HttpMethod.GET, "/stock/**").permitAll();

                auth.anyRequest().authenticated();
            });

        http.anonymous(AbstractHttpConfigurer::disable);
        http.addFilterBefore(new JwtValidatorFilter(jwtEntryPoint), BasicAuthenticationFilter.class);

        return http.build();
    }
}
