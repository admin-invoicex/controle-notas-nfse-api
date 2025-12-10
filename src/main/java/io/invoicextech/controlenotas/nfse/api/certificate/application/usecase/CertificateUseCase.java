package io.invoicextech.controlenotas.nfse.api.certificate.application.usecase;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Enumeration;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.invoicextech.controlenotas.nfse.api.certificate.application.dto.CertificateMetadataOutput;
import io.invoicextech.controlenotas.nfse.api.certificate.application.port.EncryptionService;
import io.invoicextech.controlenotas.nfse.api.certificate.domain.model.CertificateMetadata;
import io.invoicextech.controlenotas.nfse.api.certificate.domain.model.CertificateStorage;
import io.invoicextech.controlenotas.nfse.api.certificate.domain.repository.CertificateRepository;

@Service
public class CertificateUseCase {
    private final CertificateRepository repository;
    private final EncryptionService encryptionService;
    private final String kmsKeyId;

    public CertificateUseCase(
            CertificateRepository repository,
            EncryptionService encryptionService,
            @Value("${security.certificate.kms.key-id}") String kmsKeyId) {
        this.repository = repository;
        this.encryptionService = encryptionService;
        this.kmsKeyId = kmsKeyId;
    }

    public void storeOrUpdate(UUID companyId, byte[] pfxBytes, String password) {
        if (companyId == null) throw new IllegalArgumentException("companyId is required");
        if (pfxBytes == null || pfxBytes.length == 0)
            throw new IllegalArgumentException("Arquivo do certificado é obrigatório");
        if (password == null || password.isBlank())
            throw new IllegalArgumentException("Senha do certificado é obrigatória");

        X509Certificate cert = loadPrimaryCertificate(pfxBytes, password);
        CertificateMetadata metadata = buildMetadata(cert);

        byte[] certCiphertext = encryptionService.encrypt(pfxBytes);
        byte[] passwordCiphertext =
                encryptionService.encrypt(password.getBytes(StandardCharsets.UTF_8));

        CertificateStorage existing = repository.findByCompanyId(companyId).orElse(null);
        CertificateStorage toSave =
                new CertificateStorage(
                        existing != null ? existing.id() : null,
                        companyId,
                        certCiphertext,
                        passwordCiphertext,
                        kmsKeyId,
                        metadata,
                        existing != null ? existing.createdAt() : null,
                        existing != null ? existing.updatedAt() : null);

        repository.save(toSave);
    }

    public CertificateMetadata getMetadata(UUID companyId) {
        return repository
                .findByCompanyId(companyId)
                .map(CertificateStorage::metadata)
                .orElseThrow(
                        () ->
                                new IllegalArgumentException(
                                        "Certificado não encontrado para a empresa"));
    }

    public CertificateMetadataOutput getMetadataOutput(UUID companyId) {
        CertificateStorage storage =
                repository
                        .findByCompanyId(companyId)
                        .orElseThrow(
                                () ->
                                        new IllegalArgumentException(
                                                "Certificado não encontrado para a empresa"));
        CertificateMetadata m = storage.metadata();
        return new CertificateMetadataOutput(
                m != null ? m.subjectDn() : null,
                m != null ? m.issuerDn() : null,
                m != null ? m.validFrom() : null,
                m != null ? m.validTo() : null,
                m != null ? m.serialNumber() : null,
                m != null ? m.fingerprintSha1() : null,
                m != null ? m.fingerprintSha256() : null,
                storage.createdAt(),
                storage.updatedAt());
    }

    public KeyStore loadKeyStoreForSigning(UUID companyId) {
        CertificateStorage storage =
                repository
                        .findByCompanyId(companyId)
                        .orElseThrow(
                                () ->
                                        new IllegalArgumentException(
                                                "Certificado não encontrado para a empresa"));
        byte[] pfxBytes = encryptionService.decrypt(storage.certCiphertext());
        byte[] passwordBytes = encryptionService.decrypt(storage.passwordCiphertext());
        String password = new String(passwordBytes, StandardCharsets.UTF_8);
        return createPkcs12(pfxBytes, password);
    }

    private X509Certificate loadPrimaryCertificate(byte[] pfxBytes, String password) {
        try {
            KeyStore ks = createPkcs12(pfxBytes, password);
            Enumeration<String> aliases = ks.aliases();
            while (aliases.hasMoreElements()) {
                String alias = aliases.nextElement();
                Certificate c = ks.getCertificate(alias);
                if (c instanceof X509Certificate x509) {
                    return x509;
                }
            }
            throw new IllegalArgumentException("Certificado X509 não encontrado no PFX");
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalArgumentException("PFX inválido ou senha incorreta");
        }
    }

    private KeyStore createPkcs12(byte[] pfxBytes, String password) {
        try {
            KeyStore ks = KeyStore.getInstance("PKCS12");
            ks.load(new ByteArrayInputStream(pfxBytes), password.toCharArray());
            return ks;
        } catch (Exception e) {
            throw new IllegalArgumentException("PFX inválido ou senha incorreta");
        }
    }

    private CertificateMetadata buildMetadata(X509Certificate cert) {
        try {
            String subjectDn = cert.getSubjectX500Principal().getName();
            String issuerDn = cert.getIssuerX500Principal().getName();
            OffsetDateTime validFrom =
                    OffsetDateTime.ofInstant(cert.getNotBefore().toInstant(), ZoneOffset.UTC);
            OffsetDateTime validTo =
                    OffsetDateTime.ofInstant(cert.getNotAfter().toInstant(), ZoneOffset.UTC);
            String serialNumber = cert.getSerialNumber().toString(16).toUpperCase();

            byte[] der = cert.getEncoded();
            String sha1 = hexDigest(der, "SHA-1");
            String sha256 = hexDigest(der, "SHA-256");
            return new CertificateMetadata(
                    subjectDn, issuerDn, validFrom, validTo, serialNumber, sha1, sha256);
        } catch (Exception e) {
            throw new IllegalArgumentException("Falha ao extrair metadados do certificado");
        }
    }

    private String hexDigest(byte[] data, String algo) throws Exception {
        MessageDigest md = MessageDigest.getInstance(algo);
        byte[] digest = md.digest(data);
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
}
