package io.invoicextech.controlenotas.nfse.api.auth.application.usecase;

import java.util.Optional;

import org.springframework.stereotype.Service;

import io.invoicextech.controlenotas.nfse.api.auth.application.dto.LoginInput;
import io.invoicextech.controlenotas.nfse.api.auth.application.dto.LoginOutput;
import io.invoicextech.controlenotas.nfse.api.auth.application.dto.UserOutput;
import io.invoicextech.controlenotas.nfse.api.auth.application.mapper.UserOutputMapper;
import io.invoicextech.controlenotas.nfse.api.auth.application.port.PasswordHasher;
import io.invoicextech.controlenotas.nfse.api.auth.application.port.TokenProvider;
import io.invoicextech.controlenotas.nfse.api.auth.domain.model.User;
import io.invoicextech.controlenotas.nfse.api.auth.domain.repository.UserRepository;

@Service
public class LoginUserUseCase {
    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final TokenProvider tokenProvider;

    public LoginUserUseCase(
            UserRepository userRepository,
            PasswordHasher passwordHasher,
            TokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
        this.tokenProvider = tokenProvider;
    }

    public LoginOutput execute(LoginInput input) {
        String username = Optional.ofNullable(input.username()).orElse("").trim().toLowerCase();
        String password = Optional.ofNullable(input.password()).orElse("");

        Optional<User> byEmail = userRepository.findByEmail(username);
        Optional<User> byDoc =
                byEmail.isPresent()
                        ? byEmail
                        : userRepository.findByDocument(username.replaceAll("\\D", ""));
        User user = byDoc.orElseThrow(() -> new IllegalArgumentException("Credenciais inválidas"));

        if (!passwordHasher.matches(password, user.getPasswordHash())) {
            throw new IllegalArgumentException("Credenciais inválidas");
        }

        String token =
                tokenProvider.generateToken(
                        user.getId(),
                        user.getEmail(),
                        user.getRoles(),
                        user.getDocumentType().name());
        String refreshToken = tokenProvider.generateRefreshToken(user.getId(), user.getEmail());
        long expiresIn = tokenProvider.getExpirationMillis() / 1000L;
        UserOutput userOut = UserOutputMapper.from(user);
        return new LoginOutput(token, refreshToken, "Bearer", expiresIn, userOut);
    }
}
