package io.invoicextech.controlenotas.nfse.api.auth.application.dto;

public record LoginOutput(
        String accessToken,
        String refreshToken,
        String tokenType,
        long expiresIn,
        UserOutput user) {}
