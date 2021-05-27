package com.etz.fraudeagleeyemanager.dto.request;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class MapRuleToProductRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	@NotNull(message="productCode cannot be empty")
	private String productCode;
	@NotNull(message="ruleId cannot be empty")
	private Integer ruleId;
	private Boolean notifyAdmin;
	private Integer emailGroupId;
	private Boolean notifyCustomer;
	@NotNull(message="authorised cannot be empty")
	private Boolean authorised;
	@NotNull(message="createdBy cannot be empty")
	private String createdBy;

}
