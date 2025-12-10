package io.invoicextech.controlenotas.nfse.api.certificate.infrastructure.persistence.mapper;

import io.invoicextech.controlenotas.nfse.api.certificate.domain.model.CertificateMetadata;
import io.invoicextech.controlenotas.nfse.api.certificate.domain.model.CertificateStorage;
import io.invoicextech.controlenotas.nfse.api.certificate.infrastructure.persistence.entity.CertificateEntity;

public final class CertificateMapper {
    private CertificateMapper() {}

    public static CertificateEntity toEntity(CertificateStorage d) {
        CertificateEntity e = new CertificateEntity();
        e.setId(d.id());
        e.setCompanyId(d.companyId());
        e.setCertCiphertext(d.certCiphertext());
        e.setPasswordCiphertext(d.passwordCiphertext());
        e.setKmsKeyId(d.kmsKeyId());
        if (d.metadata() != null) {
            CertificateMetadata m = d.metadata();
            e.setSubjectDn(m.subjectDn());
            e.setIssuerDn(m.issuerDn());
            e.setValidFrom(m.validFrom());
            e.setValidTo(m.validTo());
            e.setSerialNumber(m.serialNumber());
            e.setFingerprintSha1(m.fingerprintSha1());
            e.setFingerprintSha256(m.fingerprintSha256());
        }
        e.setCreatedAt(d.createdAt());
        e.setUpdatedAt(d.updatedAt());
        return e;
    }

    public static CertificateStorage toDomain(CertificateEntity e) {
        CertificateMetadata m =
                new CertificateMetadata(
                        e.getSubjectDn(),
                        e.getIssuerDn(),
                        e.getValidFrom(),
                        e.getValidTo(),
                        e.getSerialNumber(),
                        e.getFingerprintSha1(),
                        e.getFingerprintSha256());

        return new CertificateStorage(
                e.getId(),
                e.getCompanyId(),
                e.getCertCiphertext(),
                e.getPasswordCiphertext(),
                e.getKmsKeyId(),
                m,
                e.getCreatedAt(),
                e.getUpdatedAt());
    }
}
