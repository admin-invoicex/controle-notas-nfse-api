package io.invoicextech.controlenotas.nfse.api.auth.rest.response;

public record AuthResponse(String accessToken, String tokenType, long expiresIn, UserResponse user) {}
