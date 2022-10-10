package com.etz.fraudeagleeyemanager.dto.response;

import com.etz.fraudeagleeyemanager.entity.Rule;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({ "productRule", "createdAt", "createdBy"})
public class UpdatedRuleResponse extends Rule {
	private static final long serialVersionUID = 1L;
}
