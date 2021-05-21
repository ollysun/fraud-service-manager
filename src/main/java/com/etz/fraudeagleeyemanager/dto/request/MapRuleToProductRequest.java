package com.etz.fraudeagleeyemanager.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class MapRuleToProductRequest {

	@NotBlank(message="productCode cannot be empty")
	private String productCode;
	@NotNull(message="ruleId cannot be empty")
	private Long ruleId;
	private Boolean notifyAdmin;
	private Long emailGroupId;
	private Boolean notifyCustomer;
	@NotNull(message="authorised cannot be empty")
	private Boolean authorised;
	@NotNull(message="createdBy cannot be empty")
	private String createdBy;

}
