package io.invoicextech.controlenotas.nfse.api.certificate.infrastructure.persistence.adapter;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import io.invoicextech.controlenotas.nfse.api.certificate.domain.model.CertificateStorage;
import io.invoicextech.controlenotas.nfse.api.certificate.domain.repository.CertificateRepository;
import io.invoicextech.controlenotas.nfse.api.certificate.infrastructure.persistence.entity.CertificateEntity;
import io.invoicextech.controlenotas.nfse.api.certificate.infrastructure.persistence.mapper.CertificateMapper;
import io.invoicextech.controlenotas.nfse.api.certificate.infrastructure.persistence.repository.CertificateJpaRepository;

@Repository
public class CertificateRepositoryAdapter implements CertificateRepository {
    private final CertificateJpaRepository jpaRepository;

    public CertificateRepositoryAdapter(CertificateJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public CertificateStorage save(CertificateStorage certificate) {
        OffsetDateTime now = OffsetDateTime.now();
        boolean isNew = certificate.id() == null;
        CertificateStorage toPersist =
                new CertificateStorage(
                        isNew ? UUID.randomUUID() : certificate.id(),
                        certificate.companyId(),
                        certificate.certCiphertext(),
                        certificate.passwordCiphertext(),
                        certificate.kmsKeyId(),
                        certificate.metadata(),
                        isNew ? now : certificate.createdAt(),
                        now);

        CertificateEntity saved = jpaRepository.save(CertificateMapper.toEntity(toPersist));
        return CertificateMapper.toDomain(saved);
    }

    @Override
    public Optional<CertificateStorage> findByCompanyId(UUID companyId) {
        return jpaRepository.findByCompanyId(companyId).map(CertificateMapper::toDomain);
    }

    @Override
    public boolean existsByCompanyId(UUID companyId) {
        return jpaRepository.existsByCompanyId(companyId);
    }
}
