package com.mercury.pas.service;

import com.mercury.pas.model.dto.GenerateQuoteRequest;
import com.mercury.pas.model.dto.QuoteResponse;
import com.mercury.pas.model.dto.SaveQuoteRequest;

import java.util.List;

public interface QuoteService {
    QuoteResponse generate(GenerateQuoteRequest request);
    QuoteResponse save(SaveQuoteRequest request);
    QuoteResponse getById(Long id);
    List<QuoteResponse> getByCustomer(Long customerId);
    Long convertToPolicy(Long quoteId, Long agentId);
}

