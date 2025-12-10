package io.invoicextech.controlenotas.nfse.api.auth.application.usecase;

import org.springframework.stereotype.Service;

import io.invoicextech.controlenotas.nfse.api.auth.application.dto.LoginOutput;
import io.invoicextech.controlenotas.nfse.api.auth.application.dto.UserOutput;
import io.invoicextech.controlenotas.nfse.api.auth.application.mapper.UserOutputMapper;
import io.invoicextech.controlenotas.nfse.api.auth.application.port.TokenProvider;
import io.invoicextech.controlenotas.nfse.api.auth.domain.model.User;
import io.invoicextech.controlenotas.nfse.api.auth.domain.repository.UserRepository;

@Service
public class RefreshTokenUseCase {
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;

    public RefreshTokenUseCase(UserRepository userRepository, TokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
    }

    public LoginOutput execute(String refreshToken) {
        if (!tokenProvider.validateRefreshToken(refreshToken)) {
            throw new IllegalArgumentException("Refresh token inválido");
        }
        String subject = tokenProvider.getRefreshSubject(refreshToken);
        User user =
                userRepository
                        .findByEmail(subject)
                        .orElseThrow(
                                () ->
                                        new IllegalArgumentException(
                                                "Usuário não encontrado para o refresh token"));

        String newAccess =
                tokenProvider.generateToken(
                        user.getId(),
                        user.getEmail(),
                        user.getRoles(),
                        user.getDocumentType().name());
        String newRefresh = tokenProvider.generateRefreshToken(user.getId(), user.getEmail());
        long expiresIn = tokenProvider.getExpirationMillis() / 1000L;
        UserOutput userOut = UserOutputMapper.from(user);
        return new LoginOutput(newAccess, newRefresh, "Bearer", expiresIn, userOut);
    }
}
