package io.invoicextech.controlenotas.nfse.api.certificate.infrastructure.persistence.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import io.invoicextech.controlenotas.nfse.api.certificate.infrastructure.persistence.entity.CertificateEntity;

public interface CertificateJpaRepository extends JpaRepository<CertificateEntity, UUID> {
    Optional<CertificateEntity> findByCompanyId(UUID companyId);

    boolean existsByCompanyId(UUID companyId);
}
