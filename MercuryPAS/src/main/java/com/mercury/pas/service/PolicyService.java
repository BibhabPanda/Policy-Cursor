package com.mercury.pas.service;

import com.mercury.pas.model.dto.CreatePolicyRequest;
import com.mercury.pas.model.dto.PolicyResponse;

import java.util.List;

public interface PolicyService {
    PolicyResponse create(CreatePolicyRequest request);
    PolicyResponse getById(Long id);
    List<PolicyResponse> getByCustomer(Long customerId);
    List<PolicyResponse> getByAgent(Long agentId);
    PolicyResponse update(Long id, CreatePolicyRequest request);
    void delete(Long id);
}

