package com.mercury.pas.model.entity;

import com.mercury.pas.model.enums.QuoteStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "quotes", indexes = {
        @Index(name = "idx_quoteNumber", columnList = "quoteNumber")
})
public class Quote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String quoteNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private User customer;

    @Column(precision = 12, scale = 2)
    private BigDecimal premiumAmount;

    @Lob
    private String coverageDetails;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuoteStatus status;

    @Column(nullable = false)
    private OffsetDateTime createdAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getQuoteNumber() {
		return quoteNumber;
	}

	public void setQuoteNumber(String quoteNumber) {
		this.quoteNumber = quoteNumber;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public User getCustomer() {
		return customer;
	}

	public void setCustomer(User customer) {
		this.customer = customer;
	}

	public BigDecimal getPremiumAmount() {
		return premiumAmount;
	}

	public void setPremiumAmount(BigDecimal premiumAmount) {
		this.premiumAmount = premiumAmount;
	}

	public String getCoverageDetails() {
		return coverageDetails;
	}

	public void setCoverageDetails(String coverageDetails) {
		this.coverageDetails = coverageDetails;
	}

	public QuoteStatus getStatus() {
		return status;
	}

	public void setStatus(QuoteStatus status) {
		this.status = status;
	}

	public OffsetDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(OffsetDateTime createdAt) {
		this.createdAt = createdAt;
	}
    
    
}

