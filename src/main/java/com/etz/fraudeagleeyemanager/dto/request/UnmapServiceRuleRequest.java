package com.etz.fraudeagleeyemanager.dto.request;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.*;

@Data
public class UnmapServiceRuleRequest {
	@NotBlank(message="Please enter the service id")
	private String serviceId;

	@NotNull(message="Please enter the rule Ids")
	private List<Long> ruleId;
}
