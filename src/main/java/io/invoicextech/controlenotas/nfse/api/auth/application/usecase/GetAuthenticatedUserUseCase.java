package io.invoicextech.controlenotas.nfse.api.auth.application.usecase;

import org.springframework.stereotype.Service;

import io.invoicextech.controlenotas.nfse.api.auth.application.dto.UserOutput;
import io.invoicextech.controlenotas.nfse.api.auth.application.mapper.UserOutputMapper;
import io.invoicextech.controlenotas.nfse.api.auth.application.port.AuthenticatedUserProvider;

@Service
public class GetAuthenticatedUserUseCase {
    private final AuthenticatedUserProvider authenticatedUserProvider;

    public GetAuthenticatedUserUseCase(AuthenticatedUserProvider authenticatedUserProvider) {
        this.authenticatedUserProvider = authenticatedUserProvider;
    }

    public UserOutput execute() {
        return authenticatedUserProvider
                .currentUser()
                .map(UserOutputMapper::from)
                .orElseThrow(() -> new IllegalStateException("Usuário não autenticado"));
    }
}
