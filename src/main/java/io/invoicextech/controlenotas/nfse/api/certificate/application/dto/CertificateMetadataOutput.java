package io.invoicextech.controlenotas.nfse.api.certificate.application.dto;

import java.time.OffsetDateTime;

public record CertificateMetadataOutput(
        String subjectDn,
        String issuerDn,
        OffsetDateTime validFrom,
        OffsetDateTime validTo,
        String serialNumber,
        String fingerprintSha1,
        String fingerprintSha256,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt) {}
