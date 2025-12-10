package io.invoicextech.controlenotas.nfse.api.certificate.infrastructure.persistence.entity;

import java.time.OffsetDateTime;
import java.util.UUID;

import jakarta.persistence.*;

@Entity
@Table(name = "certificate_storage")
public class CertificateEntity {
    @Id
    @Column(nullable = false)
    private UUID id;

    @Column(name = "company_id", nullable = false, unique = true)
    private UUID companyId;

    @Lob
    @Column(name = "cert_ciphertext", nullable = false)
    private byte[] certCiphertext;

    @Lob
    @Column(name = "password_ciphertext", nullable = false)
    private byte[] passwordCiphertext;

    @Column(name = "kms_key_id", nullable = false)
    private String kmsKeyId;

    // Metadata
    @Column(name = "subject_dn")
    private String subjectDn;

    @Column(name = "issuer_dn")
    private String issuerDn;

    @Column(name = "valid_from")
    private OffsetDateTime validFrom;

    @Column(name = "valid_to")
    private OffsetDateTime validTo;

    @Column(name = "serial_number", length = 64)
    private String serialNumber;

    @Column(name = "fingerprint_sha1", length = 64)
    private String fingerprintSha1;

    @Column(name = "fingerprint_sha256", length = 128)
    private String fingerprintSha256;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getCompanyId() {
        return companyId;
    }

    public void setCompanyId(UUID companyId) {
        this.companyId = companyId;
    }

    public byte[] getCertCiphertext() {
        return certCiphertext;
    }

    public void setCertCiphertext(byte[] certCiphertext) {
        this.certCiphertext = certCiphertext;
    }

    public byte[] getPasswordCiphertext() {
        return passwordCiphertext;
    }

    public void setPasswordCiphertext(byte[] passwordCiphertext) {
        this.passwordCiphertext = passwordCiphertext;
    }

    public String getKmsKeyId() {
        return kmsKeyId;
    }

    public void setKmsKeyId(String kmsKeyId) {
        this.kmsKeyId = kmsKeyId;
    }

    public String getSubjectDn() {
        return subjectDn;
    }

    public void setSubjectDn(String subjectDn) {
        this.subjectDn = subjectDn;
    }

    public String getIssuerDn() {
        return issuerDn;
    }

    public void setIssuerDn(String issuerDn) {
        this.issuerDn = issuerDn;
    }

    public OffsetDateTime getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(OffsetDateTime validFrom) {
        this.validFrom = validFrom;
    }

    public OffsetDateTime getValidTo() {
        return validTo;
    }

    public void setValidTo(OffsetDateTime validTo) {
        this.validTo = validTo;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getFingerprintSha1() {
        return fingerprintSha1;
    }

    public void setFingerprintSha1(String fingerprintSha1) {
        this.fingerprintSha1 = fingerprintSha1;
    }

    public String getFingerprintSha256() {
        return fingerprintSha256;
    }

    public void setFingerprintSha256(String fingerprintSha256) {
        this.fingerprintSha256 = fingerprintSha256;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
