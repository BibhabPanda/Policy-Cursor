package com.mercury.pas.controller;

import com.mercury.pas.model.dto.CreatePolicyRequest;
import com.mercury.pas.model.dto.PolicyResponse;
import com.mercury.pas.service.PolicyPdfService;
import com.mercury.pas.service.PolicyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/policies")
@Validated
public class PolicyController {
    private final PolicyService policyService;
    private final PolicyPdfService policyPdfService;

    public PolicyController(PolicyService policyService, PolicyPdfService policyPdfService) {
        this.policyService = policyService;
        this.policyPdfService = policyPdfService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('AGENT','ADMIN')")
    public ResponseEntity<PolicyResponse> create(@Valid @RequestBody CreatePolicyRequest request) {
        return ResponseEntity.ok(policyService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PolicyResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(policyService.getById(id));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<PolicyResponse>> byCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(policyService.getByCustomer(customerId));
    }

    @GetMapping("/agent/{agentId}")
    @PreAuthorize("hasAnyRole('AGENT','ADMIN')")
    public ResponseEntity<List<PolicyResponse>> byAgent(@PathVariable Long agentId) {
        return ResponseEntity.ok(policyService.getByAgent(agentId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('AGENT','ADMIN')")
    public ResponseEntity<PolicyResponse> update(@PathVariable Long id, @Valid @RequestBody CreatePolicyRequest request) {
        return ResponseEntity.ok(policyService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('AGENT','ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        policyService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> generatePdf(@PathVariable Long id) {
        byte[] pdfBytes = policyPdfService.generatePolicyPdf(id).toByteArray();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "policy_" + id + ".pdf");
        return ResponseEntity.ok().headers(headers).body(pdfBytes);
    }
}

