package com.etz.fraudeagleeyemanager.dto.request;

import com.etz.fraudeagleeyemanager.constant.SuspicionLevel;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CreateRuleRequest {
	@NotNull(message="firstSourceVal cannot be empty")
	private String firstSourceVal;
	@NotNull(message="firstOperator cannot be empty")
	private String firstOperator;
	@NotBlank(message="firstCompareVal cannot be empty")
	private String firstCompareVal;
	@NotBlank(message="firstDataSourceVal cannot be empty")
	private String firstDataSourceVal;
	private String logicOperator;
	private String secondSourceVal;
	private String secondOperator;
	private String secondCompareVal;
	private String secondDataSourceVal;
	@NotNull(message="suspicion level cannot be empty")
	private Integer suspicion;
	@NotBlank(message="action cannot be empty")
	private String action;
	@NotBlank(message="createdBy cannot be empty")
	private String createdBy;
	@NotNull(message="authorised cannot be empty")
	private Boolean authorised;
}
