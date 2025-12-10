package io.invoicextech.controlenotas.nfse.api.auth.infrastructure.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import io.invoicextech.controlenotas.nfse.api.auth.application.port.PasswordHasher;

@Component
public class PasswordHasherImpl implements PasswordHasher {
    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public String hash(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    @Override
    public boolean matches(String rawPassword, String hashed) {
        return encoder.matches(rawPassword, hashed);
    }
}
