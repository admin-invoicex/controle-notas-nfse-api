package io.invoicextech.controlenotas.nfse.api.auth.application.mapper;

import io.invoicextech.controlenotas.nfse.api.auth.application.dto.UserOutput;
import io.invoicextech.controlenotas.nfse.api.auth.domain.model.User;

import java.util.stream.Collectors;

public final class UserOutputMapper {
    private UserOutputMapper() {}

    public static UserOutput from(User u) {
        return new UserOutput(
                u.getId(),
                u.getName(),
                u.getEmail(),
                u.getDocument(),
                u.getDocumentType().name(),
                u.isActive(),
                u.getRoles().stream().map(Enum::name).collect(Collectors.toSet())
        );
    }
}
