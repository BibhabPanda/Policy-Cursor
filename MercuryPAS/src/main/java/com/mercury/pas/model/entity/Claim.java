package com.mercury.pas.model.entity;

import com.mercury.pas.model.enums.ClaimStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.Builder;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "claims", indexes = {
        @Index(name = "idx_claimNumber", columnList = "claimNumber")
})
public class Claim {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String claimNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_id")
    private Policy policy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private User customer;

    @Lob
    private String description;

    @Enumerated(EnumType.STRING)
    private ClaimStatus status;

    @ElementCollection
    @CollectionTable(name = "claim_documents", joinColumns = @JoinColumn(name = "claim_id"))
    @Column(name = "document_path")
    private List<String> documentPaths = new ArrayList<>();

    private OffsetDateTime createdAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getClaimNumber() {
		return claimNumber;
	}

	public void setClaimNumber(String claimNumber) {
		this.claimNumber = claimNumber;
	}

	public Policy getPolicy() {
		return policy;
	}

	public void setPolicy(Policy policy) {
		this.policy = policy;
	}

	public User getCustomer() {
		return customer;
	}

	public void setCustomer(User customer) {
		this.customer = customer;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ClaimStatus getStatus() {
		return status;
	}

	public void setStatus(ClaimStatus status) {
		this.status = status;
	}

	public List<String> getDocumentPaths() {
		return documentPaths;
	}

	public void setDocumentPaths(List<String> documentPaths) {
		this.documentPaths = documentPaths;
	}

	public OffsetDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(OffsetDateTime createdAt) {
		this.createdAt = createdAt;
	}
    
    
}

