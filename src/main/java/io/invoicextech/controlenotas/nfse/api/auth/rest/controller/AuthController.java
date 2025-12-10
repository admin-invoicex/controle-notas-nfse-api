package io.invoicextech.controlenotas.nfse.api.auth.rest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.invoicextech.controlenotas.nfse.api.auth.application.dto.LoginInput;
import io.invoicextech.controlenotas.nfse.api.auth.application.dto.LoginOutput;
import io.invoicextech.controlenotas.nfse.api.auth.application.dto.RegisterAccountantInput;
import io.invoicextech.controlenotas.nfse.api.auth.application.dto.RegisterCompanyInput;
import io.invoicextech.controlenotas.nfse.api.auth.application.dto.UserOutput;
import io.invoicextech.controlenotas.nfse.api.auth.application.port.TokenProvider;
import io.invoicextech.controlenotas.nfse.api.auth.application.usecase.GetAuthenticatedUserUseCase;
import io.invoicextech.controlenotas.nfse.api.auth.application.usecase.LoginUserUseCase;
import io.invoicextech.controlenotas.nfse.api.auth.application.usecase.RefreshTokenUseCase;
import io.invoicextech.controlenotas.nfse.api.auth.application.usecase.RegisterAccountantUserUseCase;
import io.invoicextech.controlenotas.nfse.api.auth.application.usecase.RegisterCompanyUserUseCase;
import io.invoicextech.controlenotas.nfse.api.auth.domain.model.RoleName;
import io.invoicextech.controlenotas.nfse.api.auth.rest.mapper.ResponseMapper;
import io.invoicextech.controlenotas.nfse.api.auth.rest.request.LoginRequest;
import io.invoicextech.controlenotas.nfse.api.auth.rest.request.RefreshTokenRequest;
import io.invoicextech.controlenotas.nfse.api.auth.rest.request.RegisterAccountantRequest;
import io.invoicextech.controlenotas.nfse.api.auth.rest.request.RegisterCompanyRequest;
import io.invoicextech.controlenotas.nfse.api.auth.rest.response.AuthResponse;
import io.invoicextech.controlenotas.nfse.api.auth.rest.response.UserResponse;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final RegisterCompanyUserUseCase registerCompanyUserUseCase;
    private final RegisterAccountantUserUseCase registerAccountantUserUseCase;
    private final LoginUserUseCase loginUserUseCase;
    private final GetAuthenticatedUserUseCase getAuthenticatedUserUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;
    private final TokenProvider tokenProvider;

    public AuthController(
            RegisterCompanyUserUseCase registerCompanyUserUseCase,
            RegisterAccountantUserUseCase registerAccountantUserUseCase,
            LoginUserUseCase loginUserUseCase,
            GetAuthenticatedUserUseCase getAuthenticatedUserUseCase,
            TokenProvider tokenProvider,
            RefreshTokenUseCase refreshTokenUseCase) {
        this.registerCompanyUserUseCase = registerCompanyUserUseCase;
        this.registerAccountantUserUseCase = registerAccountantUserUseCase;
        this.loginUserUseCase = loginUserUseCase;
        this.getAuthenticatedUserUseCase = getAuthenticatedUserUseCase;
        this.tokenProvider = tokenProvider;
        this.refreshTokenUseCase = refreshTokenUseCase;
    }

    @PostMapping("/register/company")
    public ResponseEntity<AuthResponse> registerCompany(
            @RequestBody @Validated RegisterCompanyRequest request) {
        UserOutput user =
                registerCompanyUserUseCase.execute(
                        new RegisterCompanyInput(
                                request.name(),
                                request.email(),
                                request.password(),
                                request.confirmPassword(),
                                request.cnpj()));
        return ResponseEntity.status(HttpStatus.CREATED).body(issueTokens(user));
    }

    @PostMapping("/register/accountant")
    public ResponseEntity<AuthResponse> registerAccountant(
            @RequestBody @Validated RegisterAccountantRequest request) {
        UserOutput user =
                registerAccountantUserUseCase.execute(
                        new RegisterAccountantInput(
                                request.name(),
                                request.email(),
                                request.password(),
                                request.confirmPassword(),
                                request.cpf()));
        return ResponseEntity.status(HttpStatus.CREATED).body(issueTokens(user));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Validated LoginRequest request) {
        LoginOutput out =
                loginUserUseCase.execute(new LoginInput(request.username(), request.password()));
        return ResponseEntity.ok(ResponseMapper.toResponse(out));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> me() {
        UserOutput user = getAuthenticatedUserUseCase.execute();
        return ResponseEntity.ok(ResponseMapper.toResponse(user));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(
            @RequestBody @Validated RefreshTokenRequest request) {
        LoginOutput out = refreshTokenUseCase.execute(request.refreshToken());
        return ResponseEntity.ok(ResponseMapper.toResponse(out));
    }

    private AuthResponse issueTokens(UserOutput user) {
        String token =
                tokenProvider.generateToken(
                        user.id(),
                        user.email(),
                        user.roles().stream()
                                .map(RoleName::valueOf)
                                .collect(java.util.stream.Collectors.toSet()),
                        user.documentType());
        String refresh = tokenProvider.generateRefreshToken(user.id(), user.email());
        long expiresIn = tokenProvider.getExpirationMillis() / 1000L;
        LoginOutput out = new LoginOutput(token, refresh, "Bearer", expiresIn, user);
        return ResponseMapper.toResponse(out);
    }
}
