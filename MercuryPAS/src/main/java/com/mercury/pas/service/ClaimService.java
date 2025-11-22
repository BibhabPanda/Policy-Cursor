package com.mercury.pas.service;


import java.util.List;

import com.mercury.pas.model.dto.ClaimResponse;
import com.mercury.pas.model.dto.FileClaimRequest;
import com.mercury.pas.model.dto.UploadDocumentRequest;

public interface ClaimService {
    ClaimResponse fileClaim(FileClaimRequest request);
    ClaimResponse getById(Long id);
    List<ClaimResponse> getByPolicy(Long policyId);
    ClaimResponse uploadDocument(Long claimId, UploadDocumentRequest request);
}

