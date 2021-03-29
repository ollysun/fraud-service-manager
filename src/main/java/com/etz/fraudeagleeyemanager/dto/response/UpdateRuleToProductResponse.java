package com.etz.fraudeagleeyemanager.dto.response;

import com.etz.fraudengine.dto.request.RuleToProductRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class UpdateRuleToProductResponse extends RuleToProductRequest {

	@JsonProperty("productRuleID")
	private Integer productRuleId;
	private String updatedBy;
}
