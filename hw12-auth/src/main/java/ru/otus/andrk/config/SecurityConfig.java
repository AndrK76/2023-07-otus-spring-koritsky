package ru.otus.andrk.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(request -> {
            request.requestMatchers(
                    antMatcher("/css/*"),
                    antMatcher("/webjars/**")
            ).permitAll();
            request.anyRequest().authenticated();
        });

        http.formLogin(form -> {
            form.loginPage("/login");
            form.permitAll();
        });

        return http.build();
    }
}
