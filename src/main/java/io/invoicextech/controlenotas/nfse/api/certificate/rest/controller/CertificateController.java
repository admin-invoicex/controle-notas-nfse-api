package io.invoicextech.controlenotas.nfse.api.certificate.rest.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.invoicextech.controlenotas.nfse.api.certificate.application.dto.CertificateMetadataOutput;
import io.invoicextech.controlenotas.nfse.api.certificate.application.usecase.CertificateUseCase;
import io.invoicextech.controlenotas.nfse.api.certificate.rest.mapper.CertificateResponseMapper;
import io.invoicextech.controlenotas.nfse.api.certificate.rest.response.CertificateMetadataResponse;

@RestController
@RequestMapping("/api/v1/companies/{companyId}/certificates")
@Validated
public class CertificateController {

    private final CertificateUseCase certificateService;

    public CertificateController(CertificateUseCase certificateService) {
        this.certificateService = certificateService;
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<Void> upload(
            @PathVariable("companyId") UUID companyId,
            @RequestPart("file") MultipartFile file,
            @RequestPart("password") String password)
            throws Exception {
        byte[] pfxBytes = (file != null ? file.getBytes() : null);
        certificateService.storeOrUpdate(companyId, pfxBytes, password);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/metadata")
    public ResponseEntity<CertificateMetadataResponse> getMetadata(
            @PathVariable("companyId") UUID companyId) {
        CertificateMetadataOutput out = certificateService.getMetadataOutput(companyId);
        CertificateMetadataResponse resp = CertificateResponseMapper.toResponse(out);
        return ResponseEntity.ok(resp);
    }
}
