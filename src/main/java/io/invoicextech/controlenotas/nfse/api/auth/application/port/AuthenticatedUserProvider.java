package io.invoicextech.controlenotas.nfse.api.auth.application.port;

import java.util.Optional;

import io.invoicextech.controlenotas.nfse.api.auth.domain.model.User;

public interface AuthenticatedUserProvider {
    Optional<User> currentUser();
}
