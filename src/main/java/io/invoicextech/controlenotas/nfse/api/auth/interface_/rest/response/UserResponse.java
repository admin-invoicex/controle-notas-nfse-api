package io.invoicextech.controlenotas.nfse.api.auth.interface_.rest.response;

import java.util.Set;

public record UserResponse(Long id, String name, String email, String document, String documentType, boolean active, Set<String> roles) {}
