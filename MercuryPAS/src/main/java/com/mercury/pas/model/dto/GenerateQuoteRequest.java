package com.mercury.pas.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class GenerateQuoteRequest {

    @NotNull Long customerId;
    @NotBlank String make;
    @NotBlank String model;
    @NotNull Integer year;
    @NotBlank String vin;
    @NotNull Integer driverAge;

    public Long getCustomerId() {
        return customerId;
    }
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
    public String getMake() {
        return make;
    }
    public void setMake(String make) {
        this.make = make;
    }
    public String getModel() {
        return model;
    }
    public void setModel(String model) {
        this.model = model;
    }
    public Integer getYear() {
        return year;
    }
    public void setYear(Integer year) {
        this.year = year;
    }
    public String getVin() {
        return vin;
    }
    public void setVin(String vin) {
        this.vin = vin;
    }
    public Integer getDriverAge() {
        return driverAge;
    }
    public void setDriverAge(Integer driverAge) {
        this.driverAge = driverAge;
    }


}