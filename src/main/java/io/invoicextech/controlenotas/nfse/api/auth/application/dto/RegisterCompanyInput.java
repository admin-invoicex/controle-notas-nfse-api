package io.invoicextech.controlenotas.nfse.api.auth.application.dto;

public record RegisterCompanyInput(
        String name, String email, String password, String confirmPassword, String cnpj) {}
