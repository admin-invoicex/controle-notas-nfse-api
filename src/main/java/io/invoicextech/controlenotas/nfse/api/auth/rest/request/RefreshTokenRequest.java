package io.invoicextech.controlenotas.nfse.api.auth.rest.request;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(@NotBlank String refreshToken) {}
