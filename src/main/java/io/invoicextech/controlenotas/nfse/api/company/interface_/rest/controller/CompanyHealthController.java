package io.invoicextech.controlenotas.nfse.api.company.interface_.rest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/company")
public class CompanyHealthController {
    @GetMapping("/health")
    public ResponseEntity<String> health() { return ResponseEntity.ok("company-ok"); }
}
