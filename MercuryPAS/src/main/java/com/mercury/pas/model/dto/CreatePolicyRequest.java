package com.mercury.pas.model.dto;

import com.mercury.pas.model.enums.PolicyTerm;
import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

public class CreatePolicyRequest {

    @NotNull Long quoteId;

    @NotNull Long agentId;

    @NotNull LocalDate startDate;

    @NotNull LocalDate endDate;

    @NotNull PolicyTerm term;

    @NotNull String stateCode;

    public Long getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(Long quoteId) {
        this.quoteId = quoteId;
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

    public PolicyTerm getTerm() {
        return term;
    }

    public void setTerm(PolicyTerm term) {
        this.term = term;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }
}