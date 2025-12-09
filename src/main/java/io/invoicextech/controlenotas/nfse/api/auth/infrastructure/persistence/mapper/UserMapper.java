package io.invoicextech.controlenotas.nfse.api.auth.infrastructure.persistence.mapper;

import io.invoicextech.controlenotas.nfse.api.auth.domain.model.DocumentType;
import io.invoicextech.controlenotas.nfse.api.auth.domain.model.RoleName;
import io.invoicextech.controlenotas.nfse.api.auth.domain.model.User;
import io.invoicextech.controlenotas.nfse.api.auth.infrastructure.persistence.entity.RoleEntity;
import io.invoicextech.controlenotas.nfse.api.auth.infrastructure.persistence.entity.UserEntity;

import java.util.Set;
import java.util.stream.Collectors;

public final class UserMapper {
    private UserMapper() {}

    public static UserEntity toEntity(User domain, Set<RoleEntity> roles) {
        UserEntity e = new UserEntity();
        e.setId(domain.getId());
        e.setName(domain.getName());
        e.setEmail(domain.getEmail());
        e.setPasswordHash(domain.getPasswordHash());
        e.setDocument(domain.getDocument());
        e.setDocumentType(domain.getDocumentType().name());
        e.setActive(domain.isActive());
        e.setCreatedAt(domain.getCreatedAt());
        e.setUpdatedAt(domain.getUpdatedAt());
        e.setRoles(roles);
        return e;
    }

    public static User toDomain(UserEntity e) {
        User u = User.newUser(
                e.getName(),
                e.getEmail(),
                e.getPasswordHash(),
                e.getDocument(),
                DocumentType.valueOf(e.getDocumentType()),
                e.getRoles().stream().map(r -> RoleName.valueOf(r.getName())).collect(Collectors.toSet())
        );
        u.setId(e.getId());
        u.setActive(e.isActive());
        u.setCreatedAt(e.getCreatedAt());
        u.setUpdatedAt(e.getUpdatedAt());
        return u;
    }
}
