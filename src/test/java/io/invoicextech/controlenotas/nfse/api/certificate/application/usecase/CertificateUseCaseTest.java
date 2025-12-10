package io.invoicextech.controlenotas.nfse.api.certificate.application.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.invoicextech.controlenotas.nfse.api.certificate.application.port.EncryptionService;
import io.invoicextech.controlenotas.nfse.api.certificate.domain.repository.CertificateRepository;

public class CertificateUseCaseTest {

    private CertificateRepository repository;
    private EncryptionService encryptionService;
    private CertificateUseCase service;

    @BeforeEach
    void setup() {
        repository = Mockito.mock(CertificateRepository.class);
        encryptionService = Mockito.mock(EncryptionService.class);
        service = new CertificateUseCase(repository, encryptionService, "alias/test");
    }

    @Test
    @DisplayName("storeOrUpdate deve validar pfx vazio")
    void storeOrUpdate_emptyPfx_shouldThrow() {
        UUID companyId = UUID.randomUUID();
        IllegalArgumentException ex =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> service.storeOrUpdate(companyId, new byte[0], "pass"));
        assertTrue(ex.getMessage().contains("Arquivo do certificado"));
    }

    @Test
    @DisplayName("storeOrUpdate deve validar password vazio")
    void storeOrUpdate_emptyPassword_shouldThrow() {
        UUID companyId = UUID.randomUUID();
        IllegalArgumentException ex =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> service.storeOrUpdate(companyId, new byte[] {1, 2, 3}, " "));
        assertTrue(ex.getMessage().contains("Senha do certificado"));
    }

    @Test
    @DisplayName("getMetadata deve lançar quando não encontrado")
    void getMetadata_notFound_shouldThrow() {
        when(repository.findByCompanyId(any())).thenReturn(Optional.empty());
        UUID companyId = UUID.randomUUID();
        assertThrows(IllegalArgumentException.class, () -> service.getMetadata(companyId));
    }

    @Test
    @DisplayName("loadKeyStoreForSigning deve lançar quando não encontrado")
    void loadKeyStore_notFound_shouldThrow() {
        when(repository.findByCompanyId(any())).thenReturn(Optional.empty());
        UUID companyId = UUID.randomUUID();
        assertThrows(
                IllegalArgumentException.class, () -> service.loadKeyStoreForSigning(companyId));
    }
}
