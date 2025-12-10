package io.invoicextech.controlenotas.nfse.api.certificate.domain.repository;

import java.util.Optional;
import java.util.UUID;

import io.invoicextech.controlenotas.nfse.api.certificate.domain.model.CertificateStorage;

public interface CertificateRepository {
    CertificateStorage save(CertificateStorage certificate);

    Optional<CertificateStorage> findByCompanyId(UUID companyId);

    boolean existsByCompanyId(UUID companyId);
}
