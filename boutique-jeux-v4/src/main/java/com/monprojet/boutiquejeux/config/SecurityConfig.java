package com.monprojet.boutiquejeux.config;

import com.monprojet.boutiquejeux.security.jwt.JwtAuthenticationFilter;
import com.monprojet.boutiquejeux.security.service.ClientUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final ClientUserDetailsService clientUserDetailsService;

    private static final String[] PUBLIC_GET = {
            "/api/produits", "/api/produits/**"
    };

    private static final String[] PUBLIC_POST = {
            "/api/auth/login", "/api/auth/inscription"
    };

    private static final String[] SWAGGER_PATHS = {
            "/swagger-ui/**", "/swagger-ui.html",
            "/v3/api-docs/**", "/v3/api-docs"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Spring Security 7 : méthode lambda pour AbstractHttpConfigurer
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(SWAGGER_PATHS).permitAll()
                .requestMatchers(HttpMethod.POST, PUBLIC_POST).permitAll()
                .requestMatchers(HttpMethod.GET, PUBLIC_GET).permitAll()
                .requestMatchers("/api/admin/**", "/api/stock/**", "/api/planning/**")
                    .hasAnyRole("ADMIN", "MANAGER", "VENDEUR")
                .anyRequest().authenticated()
            )
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(clientUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
