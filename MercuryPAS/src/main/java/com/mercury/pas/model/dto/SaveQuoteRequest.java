package com.mercury.pas.model.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SaveQuoteRequest {
	
	@NotNull Long customerId;
    @NotNull Long vehicleId;
    @NotBlank String coverageDetails;
    @NotNull BigDecimal premiumAmount;
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public Long getVehicleId() {
		return vehicleId;
	}
	public void setVehicleId(Long vehicleId) {
		this.vehicleId = vehicleId;
	}
	public String getCoverageDetails() {
		return coverageDetails;
	}
	public void setCoverageDetails(String coverageDetails) {
		this.coverageDetails = coverageDetails;
	}
	public BigDecimal getPremiumAmount() {
		return premiumAmount;
	}
	public void setPremiumAmount(BigDecimal premiumAmount) {
		this.premiumAmount = premiumAmount;
	}
    
    
}