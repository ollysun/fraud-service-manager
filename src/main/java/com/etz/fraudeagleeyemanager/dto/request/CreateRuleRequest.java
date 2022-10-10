package com.etz.fraudeagleeyemanager.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class CreateRuleRequest {
	private String ruleName;
	@NotBlank(message="firstSourceVal cannot be empty")
	private String firstSourceVal;
	@NotBlank(message="firstDataType cannot be empty")
	private String firstDataType;
	@NotBlank(message="firstOperator cannot be empty")
	private String firstOperator;
	@NotBlank(message="firstCompareVal cannot be empty")
	private String firstCompareVal;
	@NotBlank(message="firstDataSourceVal cannot be empty")
	private String firstDataSourceVal;
	private String logicOperator;
	private String secondSourceVal;
	private String secondDataType;
	private String secondOperator;
	private String secondCompareVal;
	private String secondDataSourceVal;
	@NotNull(message="suspicion level cannot be empty")
	private Integer suspicion;
	@JsonIgnore
	private String action;
	@JsonIgnore
	private String createdBy;
	@NotNull(message="authorised cannot be empty")
	private Boolean authorised;
}
