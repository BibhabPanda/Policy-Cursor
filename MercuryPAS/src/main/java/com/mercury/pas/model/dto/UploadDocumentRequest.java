package com.mercury.pas.model.dto;



import jakarta.validation.constraints.NotBlank;



public class UploadDocumentRequest {



	@NotBlank String path;



	public String getPath() {

		return path;

	}



	public void setPath(String path) {

		this.path = path;

	}

	

	

}