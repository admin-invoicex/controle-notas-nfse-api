package io.invoicextech.controlenotas.nfse.api.auth.rest.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank String username,
        @NotBlank String password
) {}
