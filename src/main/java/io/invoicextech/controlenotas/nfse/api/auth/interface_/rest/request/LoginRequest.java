package io.invoicextech.controlenotas.nfse.api.auth.interface_.rest.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank String username,
        @NotBlank String password
) {}
