package com.etz.fraudeagleeyemanager.dto.request;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class MapRuleToServiceRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	@NotNull(message="serviceId cannot be empty")
	private Long serviceId;
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
