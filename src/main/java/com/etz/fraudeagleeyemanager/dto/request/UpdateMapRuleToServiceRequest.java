package com.etz.fraudeagleeyemanager.dto.request;

import lombok.Data;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

@Data
public class UpdateMapRuleToServiceRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	@NotNull(message="serviceRuleId cannot be empty")
	private Long serviceRuleId;
	private Boolean notifyAdmin;
	private Long emailGroupId;
	private Boolean notifyCustomer;
	@NotNull(message="authorised cannot be empty")
	private Boolean authorised;
	@NotNull(message="status cannot be empty")
	private Boolean status;
	@NotNull(message="updatedBy cannot be empty")
	private String updatedBy;

}
