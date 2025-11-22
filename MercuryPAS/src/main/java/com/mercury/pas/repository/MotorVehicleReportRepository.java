package com.mercury.pas.repository;

import com.mercury.pas.model.entity.MotorVehicleReport;
import com.mercury.pas.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MotorVehicleReportRepository extends JpaRepository<MotorVehicleReport, Long> {
    Optional<MotorVehicleReport> findByCustomerAndLicenseNumber(User customer, String licenseNumber);
    Optional<MotorVehicleReport> findByCustomer(User customer);
}

