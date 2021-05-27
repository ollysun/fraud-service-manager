package com.etz.fraudeagleeyemanager.dto.request;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class UpdateAccountProductRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	@NotNull(message = "AccountId cannot be null")
	private Long accountId;
	@NotNull(message = "ProductCode cannot be null")
	private String productCode;
	@NotNull(message = "Please enter the status")
	private Boolean status;
	@NotNull(message = "UpdatedBy cannot be null")
	private String updatedBy;
}
