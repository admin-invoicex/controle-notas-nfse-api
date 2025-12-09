package io.invoicextech.controlenotas.nfse.api.auth.application.port;

import io.invoicextech.controlenotas.nfse.api.auth.domain.model.RoleName;

import java.util.Set;

public interface TokenProvider {
    String generateToken(Long userId, String subject, Set<RoleName> roles, String documentType);
    boolean validateToken(String token);
    String getSubject(String token);
    long getExpirationMillis();
}
