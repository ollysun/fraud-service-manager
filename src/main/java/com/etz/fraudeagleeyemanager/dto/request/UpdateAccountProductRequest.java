package com.etz.fraudeagleeyemanager.dto.request;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UpdateAccountProductRequest {
	@NotNull(message = "AccountId cannot be null")
	private Long accountId;
	@NotBlank(message = "ProductCode cannot be null")
	private String productCode;
	@NotNull(message = "Please enter the status")
	private Boolean status;
	@NotBlank(message = "UpdatedBy cannot be null")
	private String updatedBy;
}
