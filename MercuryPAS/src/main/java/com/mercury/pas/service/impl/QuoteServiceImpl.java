package com.mercury.pas.service.impl;

import com.mercury.pas.exception.NotFoundException;
import com.mercury.pas.model.dto.GenerateQuoteRequest;
import com.mercury.pas.model.dto.QuoteResponse;
import com.mercury.pas.model.dto.SaveQuoteRequest;
import com.mercury.pas.model.entity.Policy;
import com.mercury.pas.model.entity.Quote;
import com.mercury.pas.model.entity.User;
import com.mercury.pas.model.entity.Vehicle;
import com.mercury.pas.model.enums.PolicyStatus;
import com.mercury.pas.model.enums.QuoteStatus;
import com.mercury.pas.repository.PolicyRepository;
import com.mercury.pas.repository.QuoteRepository;
import com.mercury.pas.repository.UserRepository;
import com.mercury.pas.repository.VehicleRepository;
import com.mercury.pas.service.QuoteService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class QuoteServiceImpl implements QuoteService {

    private final QuoteRepository quoteRepository;
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;
    private final PolicyRepository policyRepository;
    private final ModelMapper mapper;

    public QuoteServiceImpl(
            QuoteRepository quoteRepository,
            VehicleRepository vehicleRepository,
            UserRepository userRepository,
            PolicyRepository policyRepository,
            ModelMapper mapper) {
        this.quoteRepository = quoteRepository;
        this.vehicleRepository = vehicleRepository;
        this.userRepository = userRepository;
        this.policyRepository = policyRepository;
        this.mapper = mapper;
    }

    @Override
    public QuoteResponse generate(GenerateQuoteRequest request) {
        User customer = userRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new NotFoundException("Customer not found"));

        Vehicle vehicle = vehicleRepository.findByVin(request.getVin()).orElseGet(() -> {
            Vehicle v = new Vehicle();
            v.setMake(request.getMake());
            v.setModel(request.getModel());
            v.setYear(request.getYear());
            v.setVin(request.getVin());
            v.setCustomer(customer);
            return vehicleRepository.save(v);
        });

        BigDecimal premium = calculatePremium(request.getDriverAge(), request.getYear());

        Quote quote = new Quote();
        quote.setQuoteNumber("MER-QUO-" + UUID.randomUUID());
        quote.setVehicle(vehicle);
        quote.setCustomer(customer);
        quote.setPremiumAmount(premium);
        quote.setCoverageDetails("Standard auto coverage");
        quote.setStatus(QuoteStatus.GENERATED);
        quote.setCreatedAt(OffsetDateTime.now());

        quoteRepository.save(quote);
        return mapper.map(quote, QuoteResponse.class);
    }

    @Override
    public QuoteResponse save(SaveQuoteRequest request) {
        User customer = userRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new NotFoundException("Customer not found"));

        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new NotFoundException("Vehicle not found"));

        Quote quote = new Quote();
        quote.setQuoteNumber("MER-QUO-" + UUID.randomUUID());
        quote.setVehicle(vehicle);
        quote.setCustomer(customer);
        quote.setPremiumAmount(request.getPremiumAmount());
        quote.setCoverageDetails(request.getCoverageDetails());
        quote.setStatus(QuoteStatus.SAVED);
        quote.setCreatedAt(OffsetDateTime.now());

        quoteRepository.save(quote);
        return mapper.map(quote, QuoteResponse.class);
    }

    @Override
    public QuoteResponse getById(Long id) {
        Quote quote = quoteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Quote not found"));
        return mapper.map(quote, QuoteResponse.class);
    }

    @Override
    public List<QuoteResponse> getByCustomer(Long customerId) {
        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> new NotFoundException("Customer not found"));

        return quoteRepository.findByCustomer(customer).stream()
                .map(q -> mapper.map(q, QuoteResponse.class))
                .toList();
    }

    @Override
    public Long convertToPolicy(Long quoteId, Long agentId) {
        Quote quote = quoteRepository.findById(quoteId)
                .orElseThrow(() -> new NotFoundException("Quote not found"));

        User agent = userRepository.findById(agentId)
                .orElseThrow(() -> new NotFoundException("Agent not found"));

        Policy policy = new Policy();
        policy.setPolicyNumber("MER-POL-" + System.currentTimeMillis());
        policy.setQuote(quote);
        policy.setVehicle(quote.getVehicle());
        policy.setCustomer(quote.getCustomer());
        policy.setAgent(agent);
        policy.setStartDate(LocalDate.now());
        policy.setEndDate(LocalDate.now().plusYears(1));
        policy.setPremiumAmount(quote.getPremiumAmount());
        policy.setStatus(PolicyStatus.ACTIVE);

        policyRepository.save(policy);

        quote.setStatus(QuoteStatus.CONVERTED);
        quoteRepository.save(quote);

        return policy.getId();
    }

    private BigDecimal calculatePremium(int driverAge, int vehicleYear) {
        BigDecimal base = BigDecimal.valueOf(1000000);
        BigDecimal premium = base;

        if (driverAge < 25) {
            premium = premium.add(base.multiply(BigDecimal.valueOf(0.20)));
        }

        int currentYear = java.time.Year.now().getValue();
        if (currentYear - vehicleYear > 10) {
            premium = premium.add(base.multiply(BigDecimal.valueOf(0.15)));
        }

        return premium;
    }

    //Add violation severity check. Reject quotes if MVR >= 3.
}