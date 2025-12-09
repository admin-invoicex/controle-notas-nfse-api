package io.invoicextech.controlenotas.nfse.api.auth.infrastructure.security;

import io.invoicextech.controlenotas.nfse.api.auth.application.port.TokenProvider;
import io.invoicextech.controlenotas.nfse.api.auth.domain.model.RoleName;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider implements TokenProvider {

    private final SecretKey key;
    private final long expirationMillis;

    public JwtTokenProvider(
            @Value("${security.jwt.secret}") String base64Secret,
            @Value("${security.jwt.expiration-ms:900000}") long expirationMillis
    ) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(base64Secret));
        this.expirationMillis = expirationMillis;
    }

    @Override
    public String generateToken(Long userId, String subject, Set<RoleName> roles, String documentType) {
        Instant now = Instant.now();
        Instant exp = now.plusMillis(expirationMillis);
        String rolesCsv = roles.stream().map(Enum::name).collect(Collectors.joining(","));
        return Jwts.builder()
                .subject(subject != null ? subject : String.valueOf(userId))
                .claim("uid", userId)
                .claim("roles", rolesCsv)
                .claim("docType", documentType)
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String getSubject(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject();
    }

    @Override
    public long getExpirationMillis() {
        return expirationMillis;
    }
}
