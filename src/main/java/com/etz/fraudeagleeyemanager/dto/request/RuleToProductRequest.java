package com.etz.fraudeagleeyemanager.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RuleToProductRequest {

	private String productCode;
	
	@JsonAlias("ruleID")
	@JsonProperty("ruleID")
	private Integer ruleId;
	private Boolean notifyAdmin;
	private Integer emailGroup;
	private Boolean notifyCustomer;
	private Boolean authorised;
	private Boolean status;
	private String createdBy;
	private String updatedBy;
	
	@JsonAlias("productRuleID")
	private Integer productRuleId;
	
}
