package com.monprojet.boutiquejeux.dto.response;
public record AuthResponse(
    String accessToken,
    String refreshToken,
    String userType,
    String email,
    String role
) {}
