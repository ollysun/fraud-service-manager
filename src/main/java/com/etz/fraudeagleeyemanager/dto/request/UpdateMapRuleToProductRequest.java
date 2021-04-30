package com.etz.fraudeagleeyemanager.dto.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateMapRuleToProductRequest {
	@NotNull(message="productRuleId cannot be empty")
	private Integer productRuleId;
	private Boolean notifyAdmin;
	private Integer emailGroupId;
	private Boolean notifyCustomer;
	@NotNull(message="authorised cannot be empty")
	private Boolean authorised;
	@NotNull(message="status cannot be empty")
	private Boolean status;
	@NotNull(message="updatedBy cannot be empty")
	private String updatedBy;

}
