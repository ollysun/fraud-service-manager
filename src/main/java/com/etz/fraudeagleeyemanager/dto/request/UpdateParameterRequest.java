package com.etz.fraudeagleeyemanager.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class UpdateParameterRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	@NotNull(message = "ParamId cannot be Null")
	@Positive(message="Please enter Number")
	private Long paramId;
	@NotBlank(message = "Name cannot be empty")
	private String name;
	@NotBlank(message = "Operator cannot be empty")
	private String operator;
	@NotNull(message = "Please set the requiredValue")
	private Boolean requireValue;
	@JsonIgnore
	private String updatedBy;
	@NotNull(message = "Authorised cannot be empty")
	private Boolean authorised;
	
}
