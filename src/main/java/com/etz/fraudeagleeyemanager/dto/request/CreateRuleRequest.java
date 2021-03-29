package com.etz.fraudeagleeyemanager.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class CreateRuleRequest {

	   private String firstSourceVal;
	   private String firstOperator;
	   private Integer firstCompareVal;
	   private String firstDataSource;
	   private String logicOperator;
	   private String secondSourceVal;
	   private String secondOperator;
	   private Integer secondCompareVal;
	   private String secondDataSource;
	   private Integer suspicion;
	   private String action;
	   private String createdBy;
	   private Boolean authorised;
	   
	   @JsonAlias("ruleID")
	   private Integer ruleId;
	   private String createdAt;
	   private String updatedBy;
	   private String updatedAt;
}
