package io.invoicextech.controlenotas.nfse.api.auth.infrastructure.security;

import io.invoicextech.controlenotas.nfse.api.auth.application.port.AuthenticatedUserProvider;
import io.invoicextech.controlenotas.nfse.api.auth.domain.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SecurityContextAuthenticatedUserProvider implements AuthenticatedUserProvider {
    @Override
    public Optional<User> currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) return Optional.empty();
        Object principal = auth.getPrincipal();
        if (principal instanceof UserPrincipal up) {
            return Optional.of(up.getUser());
        }
        return Optional.empty();
    }
}
