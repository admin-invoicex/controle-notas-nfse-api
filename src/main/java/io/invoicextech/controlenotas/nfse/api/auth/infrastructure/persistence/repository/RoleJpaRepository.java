package io.invoicextech.controlenotas.nfse.api.auth.infrastructure.persistence.repository;

import io.invoicextech.controlenotas.nfse.api.auth.infrastructure.persistence.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleJpaRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByName(String name);
}
