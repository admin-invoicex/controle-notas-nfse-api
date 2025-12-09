package io.invoicextech.controlenotas.nfse.api.auth.application.dto;

import java.util.Set;

public record UserOutput(Long id, String name, String email, String document, String documentType, boolean active, Set<String> roles) {}
