package io.invoicextech.controlenotas.nfse.api.auth.application.dto;

public record RegisterAccountantInput(String name, String email, String password, String confirmPassword, String cpf) {}
