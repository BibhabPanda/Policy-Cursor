package com.mercury.pas.controller;

import com.mercury.pas.model.dto.ClaimResponse;
import com.mercury.pas.model.dto.FileClaimRequest;
import com.mercury.pas.model.dto.UploadDocumentRequest;
import com.mercury.pas.service.ClaimService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/claims")
@Validated
public class ClaimController {
    private final ClaimService claimService;

    public ClaimController(ClaimService claimService) {
        this.claimService = claimService;
    }

    @PostMapping("/file")
    public ResponseEntity<ClaimResponse> file(@Valid @RequestBody FileClaimRequest request) {
        return ResponseEntity.ok(claimService.fileClaim(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClaimResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(claimService.getById(id));
    }

    @GetMapping("/policy/{policyId}")
    public ResponseEntity<List<ClaimResponse>> byPolicy(@PathVariable Long policyId) {
        return ResponseEntity.ok(claimService.getByPolicy(policyId));
    }

    @PostMapping("/upload-document/{claimId}")
    @PreAuthorize("hasAnyRole('AGENT','ADMIN')")
    public ResponseEntity<ClaimResponse> upload(@PathVariable Long claimId, @Valid @RequestBody UploadDocumentRequest request) {
        return ResponseEntity.ok(claimService.uploadDocument(claimId, request));
    }
}

