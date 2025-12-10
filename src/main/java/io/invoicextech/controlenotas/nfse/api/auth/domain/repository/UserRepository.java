package io.invoicextech.controlenotas.nfse.api.auth.domain.repository;

import java.util.Optional;

import io.invoicextech.controlenotas.nfse.api.auth.domain.model.User;

public interface UserRepository {
    User save(User user);

    Optional<User> findByEmail(String email);

    Optional<User> findByDocument(String document);

    boolean existsByEmail(String email);

    boolean existsByDocument(String document);
}
