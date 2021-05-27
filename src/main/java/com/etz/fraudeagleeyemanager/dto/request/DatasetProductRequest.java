package com.etz.fraudeagleeyemanager.dto.request;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class DatasetProductRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	@NotNull(message="Please enter the product Code")
	private String productCode;

	@NotNull(message="Please enter the field name")
	private String fieldName;

	@NotNull(message="Please enter the data Type")
	private String dataType;

	@NotNull(message="Please tell the compulsory status")
	private Boolean compulsory;

	@NotNull(message="Please enter the created by name")
	private String createdBy;

	@NotNull(message="Please state the status of the authorization")
	private Boolean authorised;
	
}
