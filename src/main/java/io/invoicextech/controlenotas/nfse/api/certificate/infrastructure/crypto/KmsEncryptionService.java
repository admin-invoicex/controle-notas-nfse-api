package io.invoicextech.controlenotas.nfse.api.certificate.infrastructure.crypto;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.invoicextech.controlenotas.nfse.api.certificate.application.port.EncryptionService;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kms.KmsClient;
import software.amazon.awssdk.services.kms.model.DecryptRequest;
import software.amazon.awssdk.services.kms.model.EncryptRequest;

@Service
public class KmsEncryptionService implements EncryptionService {

    private final String kmsKeyId;
    private final KmsClient kmsClient;
    private static final Map<String, String> CONTEXT =
            Map.of(
                    "service", "emiteserv",
                    "domain", "certificate");

    public KmsEncryptionService(
            @Value("${security.certificate.kms.key-id}") String kmsKeyId,
            @Value("${aws.region}") String awsRegion) {
        this.kmsKeyId = kmsKeyId;
        this.kmsClient = KmsClient.builder().region(Region.of(awsRegion)).build();
    }

    @Override
    public byte[] encrypt(byte[] plainData) {
        if (plainData == null) return null;
        EncryptRequest req =
                EncryptRequest.builder()
                        .keyId(kmsKeyId)
                        .encryptionContext(CONTEXT)
                        .plaintext(SdkBytes.fromByteArray(plainData))
                        .build();
        return kmsClient.encrypt(req).ciphertextBlob().asByteArray();
    }

    @Override
    public byte[] decrypt(byte[] cipherData) {
        if (cipherData == null) return null;
        DecryptRequest req =
                DecryptRequest.builder()
                        .encryptionContext(CONTEXT)
                        .ciphertextBlob(SdkBytes.fromByteArray(cipherData))
                        .build();
        return kmsClient.decrypt(req).plaintext().asByteArray();
    }
}
