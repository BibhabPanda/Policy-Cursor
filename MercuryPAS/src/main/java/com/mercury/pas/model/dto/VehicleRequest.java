package com.mercury.pas.model.dto;

import jakarta.validation.constraints.*;

public class VehicleRequest {
	
	@NotBlank 
	private String make;
	
	
	@NotBlank
	String model;
	
	@NotNull 
	Integer year;
	
	@NotBlank 
	String vin;

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

}