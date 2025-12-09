package io.invoicextech.controlenotas.nfse.api.auth.application.port;

public interface PasswordHasher {
    String hash(String rawPassword);
    boolean matches(String rawPassword, String hashed);
}
