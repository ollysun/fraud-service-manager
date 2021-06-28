package com.etz.fraudeagleeyemanager.dto.response;

import com.etz.fraudeagleeyemanager.dto.request.MapRuleToServiceRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class UpdateRuleToServiceResponse extends MapRuleToServiceRequest {
	private static final long serialVersionUID = 1L;

	@JsonProperty("productRuleID")
	private Integer productRuleId;
	private String updatedBy;
}
