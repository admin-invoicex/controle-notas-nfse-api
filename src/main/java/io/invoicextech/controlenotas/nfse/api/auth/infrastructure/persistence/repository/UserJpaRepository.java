package io.invoicextech.controlenotas.nfse.api.auth.infrastructure.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import io.invoicextech.controlenotas.nfse.api.auth.infrastructure.persistence.entity.UserEntity;

public interface UserJpaRepository
        extends JpaRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity> {
    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByDocument(String document);

    boolean existsByEmail(String email);

    boolean existsByDocument(String document);
}
