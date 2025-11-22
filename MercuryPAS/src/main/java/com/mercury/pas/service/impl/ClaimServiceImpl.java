package com.mercury.pas.service.impl;

import com.mercury.pas.exception.NotFoundException;
import com.mercury.pas.model.dto.ClaimResponse;
import com.mercury.pas.model.dto.FileClaimRequest;
import com.mercury.pas.model.dto.UploadDocumentRequest;
import com.mercury.pas.model.entity.Claim;
import com.mercury.pas.model.entity.Policy;
import com.mercury.pas.model.entity.User;
import com.mercury.pas.model.enums.ClaimStatus;
import com.mercury.pas.repository.ClaimRepository;
import com.mercury.pas.repository.PolicyRepository;
import com.mercury.pas.repository.UserRepository;
import com.mercury.pas.service.ClaimService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ClaimServiceImpl implements ClaimService {
    private final ClaimRepository claimRepository;
    private final PolicyRepository policyRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    public ClaimServiceImpl(ClaimRepository claimRepository, PolicyRepository policyRepository, UserRepository userRepository, ModelMapper mapper) {
        this.claimRepository = claimRepository;
        this.policyRepository = policyRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public ClaimResponse fileClaim(FileClaimRequest request) {
        Policy policy = policyRepository.findById(request.getPolicyId()).orElseThrow(() -> new NotFoundException("Policy not found"));
        User customer = userRepository.findById(request.getCustomerId()).orElseThrow(() -> new NotFoundException("Customer not found"));
        Claim claim = new Claim();
        claim.setClaimNumber(generateClaimNumber(request.getStateCode()));
        claim.setPolicy(policy);
        claim.setCustomer(customer);
        claim.setDescription(request.getDescription());
        claim.setStatus(ClaimStatus.NEW);
        claim.setCreatedAt(OffsetDateTime.now());

        claimRepository.save(claim);
        return mapper.map(claim, ClaimResponse.class);
    }

    private String generateClaimNumber(String stateCode) {
        String statePrefix = stateCode.toUpperCase();
        String uniqueId = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return statePrefix + "-CLM-" + uniqueId;
    }

    @Override
    public ClaimResponse getById(Long id) {
        Claim claim = claimRepository.findById(id).orElseThrow(() -> new NotFoundException("Claim not found"));
        return mapper.map(claim, ClaimResponse.class);
    }

    @Override
    public List<ClaimResponse> getByPolicy(Long policyId) {
        Policy policy = policyRepository.findById(policyId).orElseThrow(() -> new NotFoundException("Policy not found"));
        return claimRepository.findByPolicy(policy).stream().map(c -> mapper.map(c, ClaimResponse.class)).toList();
    }

    @Override
    public ClaimResponse uploadDocument(Long claimId, UploadDocumentRequest request) {
        Claim claim = claimRepository.findById(claimId).orElseThrow(() -> new NotFoundException("Claim not found"));
        claim.getDocumentPaths().add(request.getPath());
        claimRepository.save(claim);
        return mapper.map(claim, ClaimResponse.class);
    }
}

