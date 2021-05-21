package com.etz.fraudeagleeyemanager.dto.request;

import com.etz.fraudeagleeyemanager.constant.SuspicionLevel;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UpdateRuleRequest{
    @NotNull(message="ruleId cannot be empty")
    private Long ruleId;
    @NotNull(message="firstSourceVal cannot be empty")
    private String firstSourceVal;
    @NotNull(message="firstOperator cannot be empty")
    private String firstOperator;
    @NotNull(message="firstCompareVal cannot be empty")
    private Integer firstCompareVal;
    @NotBlank(message="firstDataSourceVal cannot be empty")
    private String firstDataSourceVal;
    private String logicOperator;
    private String secondSourceVal;
    private String secondOperator;
    private Integer secondCompareVal;
    private String secondDataSourceVal;
    @NotNull(message="suspicion cannot be empty")
    private SuspicionLevel suspicion;
    @NotNull(message="action cannot be empty")
    private String action;
    @NotNull(message="updatedBy cannot be empty")
    private String updatedBy;
    @NotNull(message="authorised cannot be empty")
    private Boolean authorised;
    @NotNull(message="status cannot be empty")
    private Boolean status;

}
