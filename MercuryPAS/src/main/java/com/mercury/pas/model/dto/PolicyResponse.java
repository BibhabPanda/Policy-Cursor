package com.mercury.pas.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.mercury.pas.model.enums.PolicyStatus;
import com.mercury.pas.model.enums.PolicyTerm;

public class PolicyResponse {

    Long id;

    String policyNumber;

    Long quoteId;

    Long vehicleId;

    Long customerId;

    Long agentId;

    LocalDate startDate;

    LocalDate endDate;

    BigDecimal premiumAmount;

    PolicyStatus status;

    PolicyTerm term;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public Long getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(Long quoteId) {
        this.quoteId = quoteId;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getPremiumAmount() {
        return premiumAmount;
    }

    public void setPremiumAmount(BigDecimal premiumAmount) {
        this.premiumAmount = premiumAmount;
    }

    public PolicyStatus getStatus() {
        return status;
    }

    public void setStatus(PolicyStatus status) {
        this.status = status;
    }

    public PolicyTerm getTerm() {
        return term;
    }

    public void setTerm(PolicyTerm term) {
        this.term = term;
    }
}