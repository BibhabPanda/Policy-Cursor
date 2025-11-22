package com.mercury.pas.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "motor_vehicle_reports", indexes = {
        @Index(name = "idx_license_number", columnList = "licenseNumber"),
        @Index(name = "idx_state", columnList = "stateCode")
})
public class MotorVehicleReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    @Column(nullable = false)
    private String licenseNumber;

    @Column(nullable = false, length = 2)
    private String stateCode;

    @Column(nullable = false)
    private Integer violations;

    @Column(nullable = false)
    private Integer accidents;

    @Column(nullable = false)
    private Integer points;

    private LocalDate reportDate;

    @Lob
    private String drivingHistory;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public Integer getViolations() {
        return violations;
    }

    public void setViolations(Integer violations) {
        this.violations = violations;
    }

    public Integer getAccidents() {
        return accidents;
    }

    public void setAccidents(Integer accidents) {
        this.accidents = accidents;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public String getDrivingHistory() {
        return drivingHistory;
    }

    public void setDrivingHistory(String drivingHistory) {
        this.drivingHistory = drivingHistory;
    }
}

