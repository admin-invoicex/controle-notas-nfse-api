package io.invoicextech.controlenotas.nfse.api.certificate.rest.mapper;

import io.invoicextech.controlenotas.nfse.api.certificate.application.dto.CertificateMetadataOutput;
import io.invoicextech.controlenotas.nfse.api.certificate.rest.response.CertificateMetadataResponse;

public final class CertificateResponseMapper {
    private CertificateResponseMapper() {}

    public static CertificateMetadataResponse toResponse(CertificateMetadataOutput out) {
        return new CertificateMetadataResponse(
                out.subjectDn(),
                out.issuerDn(),
                out.validFrom(),
                out.validTo(),
                out.serialNumber(),
                out.fingerprintSha1(),
                out.fingerprintSha256(),
                out.createdAt(),
                out.updatedAt());
    }
}
