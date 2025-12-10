package io.invoicextech.controlenotas.nfse.api.auth.application.port;

import java.util.Set;

import io.invoicextech.controlenotas.nfse.api.auth.domain.model.RoleName;

public interface TokenProvider {
    String generateToken(Long userId, String subject, Set<RoleName> roles, String documentType);

    boolean validateToken(String token);

    String getSubject(String token);

    long getExpirationMillis();

    // Refresh Token support
    String generateRefreshToken(Long userId, String subject);

    boolean validateRefreshToken(String refreshToken);

    String getRefreshSubject(String refreshToken);

    long getRefreshExpirationMillis();
}
