package com.etz.fraudeagleeyemanager.dto.request;

import lombok.Data;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

@Data
public class UpdateMapRuleToProductRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	@NotNull(message="productRuleId cannot be empty")
	private Long productRuleId;
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
