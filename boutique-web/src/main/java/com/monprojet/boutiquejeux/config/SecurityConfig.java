package com.monprojet.boutiquejeux.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final ApiAuthenticationProvider apiAuthenticationProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authenticationProvider(apiAuthenticationProvider)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/", "/catalogue/**", "/auth/**",
                    "/css/**", "/js/**", "/img/**", "/panier/**"
                ).permitAll()
                .requestMatchers("/admin/**", "/garanties/**")
                    .hasAnyRole("ADMIN", "MANAGER", "VENDEUR")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/auth/login")
                .loginProcessingUrl("/auth/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/catalogue", true)
                .failureUrl("/auth/login?error")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/auth/logout")
                .logoutSuccessUrl("/auth/login?logout")
                .invalidateHttpSession(true)
                .permitAll()
            );
        return http.build();
    }
}
