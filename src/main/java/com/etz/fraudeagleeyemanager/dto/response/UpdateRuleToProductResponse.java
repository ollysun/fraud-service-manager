package com.etz.fraudeagleeyemanager.dto.response;

import com.etz.fraudeagleeyemanager.dto.request.MapRuleToProductRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class UpdateRuleToProductResponse extends MapRuleToProductRequest {
	private static final long serialVersionUID = 1L;

	@JsonProperty("productRuleID")
	private Integer productRuleId;
	private String updatedBy;
}
