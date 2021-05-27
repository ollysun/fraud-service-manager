package com.etz.fraudeagleeyemanager.dto.request;

import com.etz.fraudeagleeyemanager.constant.DataSource;
import com.etz.fraudeagleeyemanager.constant.LogicOperator;
import com.etz.fraudeagleeyemanager.constant.SuspicionLevel;
import lombok.Data;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

@Data
public class UpdateRuleRequest implements Serializable {
	private static final long serialVersionUID = 1L;
    @NotNull(message="ruleId cannot be empty")
    private Long ruleId;
    @NotNull(message="firstSourceVal cannot be empty")
    private String firstSourceVal;
    @NotNull(message="firstOperator cannot be empty")
    private String firstOperator;
    @NotNull(message="firstCompareVal cannot be empty")
    private Integer firstCompareVal;
    @NotNull(message="firstDataSource cannot be empty")
    private DataSource firstDataSource;
    private LogicOperator logicOperator;
    private String secondSourceVal;
    private String secondOperator;
    private Integer secondCompareVal;
    private DataSource secondDataSource;
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
