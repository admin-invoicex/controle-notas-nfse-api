package io.invoicextech.controlenotas.nfse.api.auth.infrastructure.persistence.specification;

import io.invoicextech.controlenotas.nfse.api.auth.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Join;

public final class UserSpecifications {
    private UserSpecifications() {}

    public static Specification<UserEntity> hasRole(String roleName) {
        return (root, query, cb) -> {
            Join<Object, Object> roles = root.join("roles");
            return cb.equal(cb.upper(roles.get("name")), roleName.toUpperCase());
        };
    }

    public static Specification<UserEntity> isActive(boolean active) {
        return (root, query, cb) -> cb.equal(root.get("active"), active);
    }

    public static Specification<UserEntity> hasDocumentType(String documentType) {
        return (root, query, cb) -> cb.equal(cb.upper(root.get("documentType")), documentType.toUpperCase());
    }
}
