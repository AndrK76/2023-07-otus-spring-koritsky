package ru.otus.andrk.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import ru.otus.andrk.config.security.handler.CustomAccessDeniedHandler;
import ru.otus.andrk.config.security.handler.CustomAuthenticationFailureHandler;
import ru.otus.andrk.config.jwt.JwtAuthConverter;
import ru.otus.andrk.dto.mapper.ApiErrorDtoConverter;

import java.util.List;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthConverter jwtAuthConverter;

    private final CorsConfig corsConfig;

    private final ApiErrorDtoConverter converter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((auth) -> {
            auth.requestMatchers(
                    antMatcher("/error")
            ).permitAll();
            auth.anyRequest().authenticated();
        });
        http.oauth2ResourceServer((oauth2ResourceServer) -> {
            oauth2ResourceServer.jwt((jwtCustomizer) ->
                    jwtCustomizer.jwtAuthenticationConverter(jwtAuthConverter));
            oauth2ResourceServer.authenticationEntryPoint(new CustomAuthenticationFailureHandler(converter));
        });
        http.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));
        http.exceptionHandling(configurer ->
                configurer.accessDeniedHandler(new CustomAccessDeniedHandler(converter)));

        return http.build();
    }

    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(corsConfig.getOrigins());
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
