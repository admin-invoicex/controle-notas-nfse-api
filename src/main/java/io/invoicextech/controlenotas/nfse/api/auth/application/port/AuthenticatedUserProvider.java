package io.invoicextech.controlenotas.nfse.api.auth.application.port;

import io.invoicextech.controlenotas.nfse.api.auth.domain.model.User;

import java.util.Optional;

public interface AuthenticatedUserProvider {
    Optional<User> currentUser();
}
