package com.etz.fraudeagleeyemanager.dto.request;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class UpdateRuleRequest implements Serializable {
	private static final long serialVersionUID = 1L;
    @NotNull(message="ruleId cannot be empty")
    private Long ruleId;
    @NotBlank(message="firstSourceVal cannot be empty")
    private String firstSourceVal;
    @NotBlank(message="firstOperator cannot be empty")
    private String firstOperator;
    @NotNull(message="firstCompareVal cannot be empty")
    private String firstCompareVal;
    @NotBlank(message="firstDataSource cannot be empty")
    private String firstDataSource;
    private String logicOperator;
    private String secondSourceVal;
    private String secondOperator;
    private String secondCompareVal;
    private String secondDataSource;
    @NotNull(message="suspicion cannot be empty")
    private Integer suspicion;
    @NotNull(message="action cannot be empty")
    private String action;
    @NotNull(message="updatedBy cannot be empty")
    private String updatedBy;
    @NotNull(message="authorised cannot be empty")
    private Boolean authorised;
    @NotNull(message="status cannot be empty")
    private Boolean status;

}
