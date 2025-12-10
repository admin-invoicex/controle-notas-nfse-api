package io.invoicextech.controlenotas.nfse.api.certificate.rest.response;

import java.time.OffsetDateTime;

public record CertificateMetadataResponse(
        String subjectDn,
        String issuerDn,
        OffsetDateTime validFrom,
        OffsetDateTime validTo,
        String serialNumber,
        String fingerprintSha1,
        String fingerprintSha256,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt) {}
