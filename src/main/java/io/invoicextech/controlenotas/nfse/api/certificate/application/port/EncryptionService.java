package io.invoicextech.controlenotas.nfse.api.certificate.application.port;

public interface EncryptionService {
    byte[] encrypt(byte[] plainData);

    byte[] decrypt(byte[] cipherData);
}
