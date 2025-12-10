package io.invoicextech.controlenotas.nfse.api.auth.infrastructure.security;

import java.time.Instant;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.invoicextech.controlenotas.nfse.api.auth.application.port.TokenProvider;
import io.invoicextech.controlenotas.nfse.api.auth.domain.model.RoleName;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider implements TokenProvider {

    private final SecretKey key;
    private final long expirationMillis;
    private final long refreshExpirationMillis;

    public JwtTokenProvider(
            @Value("${security.jwt.secret}") String base64Secret,
            @Value("${security.jwt.expiration-ms:900000}") long expirationMillis,
            @Value("${security.jwt.refresh-expiration-ms:604800000}")
                    long refreshExpirationMillis) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(base64Secret));
        this.expirationMillis = expirationMillis;
        this.refreshExpirationMillis = refreshExpirationMillis;
    }

    @Override
    public String generateToken(
            Long userId, String subject, Set<RoleName> roles, String documentType) {
        Instant now = Instant.now();
        Instant exp = now.plusMillis(expirationMillis);
        String rolesCsv = roles.stream().map(Enum::name).collect(Collectors.joining(","));
        return Jwts.builder()
                .subject(subject != null ? subject : String.valueOf(userId))
                .claim("uid", userId)
                .claim("roles", rolesCsv)
                .claim("docType", documentType)
                .claim("typ", "access")
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
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    @Override
    public long getExpirationMillis() {
        return expirationMillis;
    }

    @Override
    public String generateRefreshToken(Long userId, String subject) {
        Instant now = Instant.now();
        Instant exp = now.plusMillis(refreshExpirationMillis);
        return Jwts.builder()
                .subject(subject != null ? subject : String.valueOf(userId))
                .claim("uid", userId)
                .claim("typ", "refresh")
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public boolean validateRefreshToken(String refreshToken) {
        try {
            var claims =
                    Jwts.parser()
                            .verifyWith(key)
                            .build()
                            .parseSignedClaims(refreshToken)
                            .getPayload();
            Object typ = claims.get("typ");
            return typ != null && "refresh".equals(typ);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String getRefreshSubject(String refreshToken) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(refreshToken)
                .getPayload()
                .getSubject();
    }

    @Override
    public long getRefreshExpirationMillis() {
        return refreshExpirationMillis;
    }
}
