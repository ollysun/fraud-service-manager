package com.etz.fraudeagleeyemanager.dto.request;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class DatasetProductRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	@NotBlank(message="Please enter the product Code")
	private String productCode;

	@NotNull(message="Please enter the service Id")
	@PositiveOrZero(message = "Please enter only Numeric number")
	private Long serviceId;

	@NotBlank(message="Please enter the field name")
	private String fieldName;

	@NotBlank(message="Please enter the data Type")
	private String dataType;

	@NotNull(message="Please tell the compulsory status")
	private Boolean compulsory;

	@JsonIgnore
	private String createdBy;

	@NotNull(message="Please state the status of the authorization")
	private Boolean authorised;
	
}
