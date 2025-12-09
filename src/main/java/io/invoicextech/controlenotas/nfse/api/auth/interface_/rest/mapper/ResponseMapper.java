package io.invoicextech.controlenotas.nfse.api.auth.interface_.rest.mapper;

import io.invoicextech.controlenotas.nfse.api.auth.application.dto.LoginOutput;
import io.invoicextech.controlenotas.nfse.api.auth.application.dto.UserOutput;
import io.invoicextech.controlenotas.nfse.api.auth.interface_.rest.response.AuthResponse;
import io.invoicextech.controlenotas.nfse.api.auth.interface_.rest.response.UserResponse;

public final class ResponseMapper {
    private ResponseMapper() {}

    public static UserResponse toResponse(UserOutput u) {
        return new UserResponse(u.id(), u.name(), u.email(), u.document(), u.documentType(), u.active(), u.roles());
    }

    public static AuthResponse toResponse(LoginOutput out) {
        return new AuthResponse(out.accessToken(), out.tokenType(), out.expiresIn(), toResponse(out.user()));
    }
}
