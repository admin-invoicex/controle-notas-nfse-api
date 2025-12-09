package io.invoicextech.controlenotas.nfse.api.auth.interface_.rest.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterCompanyRequest(
        @NotBlank String name,
        @NotBlank @Email String email,
        @NotBlank @Size(min = 8) String password,
        @NotBlank @Size(min = 8) String confirmPassword,
        @NotBlank String cnpj
) {}
