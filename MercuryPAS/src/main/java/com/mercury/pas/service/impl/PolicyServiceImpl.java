package com.mercury.pas.service.impl;

import com.mercury.pas.exception.NotFoundException;
import com.mercury.pas.model.dto.CreatePolicyRequest;
import com.mercury.pas.model.dto.PolicyResponse;
import com.mercury.pas.model.entity.Policy;
import com.mercury.pas.model.entity.Quote;
import com.mercury.pas.model.entity.User;
import com.mercury.pas.model.enums.PolicyStatus;
import com.mercury.pas.repository.PolicyRepository;
import com.mercury.pas.repository.QuoteRepository;
import com.mercury.pas.repository.UserRepository;
import com.mercury.pas.service.PolicyService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PolicyServiceImpl implements PolicyService {
    private final PolicyRepository policyRepository;
    private final QuoteRepository quoteRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    public PolicyServiceImpl(PolicyRepository policyRepository, QuoteRepository quoteRepository, UserRepository userRepository, ModelMapper mapper) {
        this.policyRepository = policyRepository;
        this.quoteRepository = quoteRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public PolicyResponse create(CreatePolicyRequest request) {
        Quote quote = quoteRepository.findById(request.getQuoteId()).orElseThrow(() -> new NotFoundException("Quote not found"));
        User agent = userRepository.findById(request.getAgentId()).orElseThrow(() -> new NotFoundException("Agent not found"));
        Policy policy = new Policy();
        policy.setPolicyNumber(generatePolicyNumber(request.getStateCode()));
        policy.setQuote(quote);
        policy.setVehicle(quote.getVehicle());
        policy.setCustomer(quote.getCustomer());
        policy.setAgent(agent);
        policy.setStartDate(request.getStartDate());
        policy.setEndDate(request.getEndDate());
        policy.setPremiumAmount(quote.getPremiumAmount());
        policy.setStatus(PolicyStatus.ACTIVE);
        policy.setTerm(request.getTerm());
        policyRepository.save(policy);
        return mapper.map(policy, PolicyResponse.class);
    }

    private String generatePolicyNumber(String stateCode) {
        String statePrefix = stateCode.toUpperCase();
        String timestamp = String.valueOf(System.currentTimeMillis());
        // Total should be 15 characters: state code (2) + "-" (1) + remaining (12)
        // Format: XX-XXXXXXXXXXXX
        String uniquePart = timestamp.substring(Math.max(0, timestamp.length() - 10));
        String policyNumber = statePrefix + "-" + uniquePart;
        
        // Ensure exactly 15 characters
        if (policyNumber.length() < 15) {
            policyNumber = policyNumber + "0".repeat(15 - policyNumber.length());
        } else if (policyNumber.length() > 15) {
            policyNumber = policyNumber.substring(0, 15);
        }
        
        return policyNumber;
    }

    @Override
    public PolicyResponse getById(Long id) {
        Policy policy = policyRepository.findById(id).orElseThrow(() -> new NotFoundException("Policy not found"));
        return mapper.map(policy, PolicyResponse.class);
    }

    @Override
    public List<PolicyResponse> getByCustomer(Long customerId) {
        User customer = userRepository.findById(customerId).orElseThrow(() -> new NotFoundException("Customer not found"));
        return policyRepository.findByCustomer(customer).stream().map(p -> mapper.map(p, PolicyResponse.class)).toList();
    }

    @Override
    public List<PolicyResponse> getByAgent(Long agentId) {
        User agent = userRepository.findById(agentId).orElseThrow(() -> new NotFoundException("Agent not found"));
        return policyRepository.findByAgent(agent).stream().map(p -> mapper.map(p, PolicyResponse.class)).toList();
    }

    @Override
    public PolicyResponse update(Long id, CreatePolicyRequest request) {
        Policy policy = policyRepository.findById(id).orElseThrow(() -> new NotFoundException("Policy not found"));
        policy.setStartDate(request.getStartDate());
        policy.setEndDate(request.getEndDate());
        policyRepository.save(policy);
        return mapper.map(policy, PolicyResponse.class);
    }

    @Override
    public void delete(Long id) {
        policyRepository.deleteById(id);
    }
}

