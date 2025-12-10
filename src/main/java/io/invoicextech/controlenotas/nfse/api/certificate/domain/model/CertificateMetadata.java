package io.invoicextech.controlenotas.nfse.api.certificate.domain.model;

import java.time.OffsetDateTime;

/** Value object holding certificate metadata. Pure domain, no framework deps. */
public record CertificateMetadata(
        String subjectDn,
        String issuerDn,
        OffsetDateTime validFrom,
        OffsetDateTime validTo,
        String serialNumber,
        String fingerprintSha1,
        String fingerprintSha256) {}
