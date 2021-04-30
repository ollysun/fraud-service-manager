package com.etz.fraudeagleeyemanager.dto.request;

import com.etz.fraudeagleeyemanager.constant.Status;
import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UpdateAccountProductRequest {
	@NotNull(message = "AccountId cannot be null")
	private Long accountId;
	@NotNull(message = "ProductCode cannot be null")
	private String productCode;
	@NotNull(message = "Please enter the status")
	private Boolean status;
	@NotNull(message = "UpdatedBy cannot be null")
	private String updatedBy;
}
