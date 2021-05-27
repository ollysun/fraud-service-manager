package com.etz.fraudeagleeyemanager.dto.request;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class CreateParameterRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	@NotNull(message = "Name cannot be empty")
	private String name;
	@NotBlank(message = "Operator cannot be empty")
	private String operator;
	@NotNull(message = "Please set the requiredValue")
	private Boolean requireValue;
	@NotBlank(message = "createdBy cannot be empty")
	private String createdBy;
	@NotNull(message = "Authorised cannot be empty")
	private Boolean authorised;
	
}
