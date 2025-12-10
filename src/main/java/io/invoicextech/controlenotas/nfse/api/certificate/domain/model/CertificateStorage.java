package io.invoicextech.controlenotas.nfse.api.certificate.domain.model;

import java.time.OffsetDateTime;
import java.util.UUID;

/** Aggregate for storing encrypted certificate and metadata. */
public record CertificateStorage(
        UUID id,
        UUID companyId,
        byte[] certCiphertext,
        byte[] passwordCiphertext,
        String kmsKeyId,
        CertificateMetadata metadata,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt) {}
