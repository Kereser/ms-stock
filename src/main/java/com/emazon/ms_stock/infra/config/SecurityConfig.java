package com.emazon.ms_stock.infra.config;

import com.emazon.ms_stock.ConsUtils;
import com.emazon.ms_stock.infra.security.entrypoint.CustomBasicAuthenticationEntryPoint;
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

    private final CustomBasicAuthenticationEntryPoint customBasicAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CustomJWTEntryPoint jwtEntryPoint) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .httpBasic(httpBasic -> httpBasic.authenticationEntryPoint(customBasicAuthenticationEntryPoint))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> {
                auth.requestMatchers(HttpMethod.POST, ConsUtils.builderPath().withArticles().withSupply().build()).hasRole(ConsUtils.AUX_DEPOT);

                auth.requestMatchers(HttpMethod.POST, ConsUtils.builderPath().withCart().withArticles().build()).hasRole(ConsUtils.CLIENT);
                auth.requestMatchers(HttpMethod.POST, ConsUtils.builderPath().withCart().withArticles().withPurchase().build()).hasRole(ConsUtils.CLIENT);
                auth.requestMatchers(HttpMethod.POST, ConsUtils.builderPath().withCart().withArticles().withRollback().build()).hasRole(ConsUtils.CLIENT);

                auth.requestMatchers(HttpMethod.GET, ConsUtils.builderPath().withCart().withArticles().withArticlesIds().build()).hasRole(ConsUtils.CLIENT);
                auth.requestMatchers(HttpMethod.POST, ConsUtils.builderPath().withArticles().withAll().build()).hasRole(ConsUtils.CLIENT);
                auth.requestMatchers(HttpMethod.GET, ConsUtils.builderPath().withArticles().withArticlesIds().build()).permitAll();

                auth.requestMatchers(HttpMethod.GET, ConsUtils.builderPath().withAnything().build()).permitAll();
                auth.requestMatchers(HttpMethod.POST, ConsUtils.builderPath().withAnything().build()).hasRole(ConsUtils.ADMIN);

                auth.anyRequest().denyAll();
            });

        http.anonymous(AbstractHttpConfigurer::disable);
        http.addFilterBefore(new JwtValidatorFilter(jwtEntryPoint), BasicAuthenticationFilter.class);

        return http.build();
    }
}
