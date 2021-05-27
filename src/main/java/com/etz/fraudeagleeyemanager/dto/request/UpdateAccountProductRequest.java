package com.etz.fraudeagleeyemanager.dto.request;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class UpdateAccountProductRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	@NotNull(message = "AccountId cannot be null")
	private Long accountId;
	@NotBlank(message = "ProductCode cannot be null")
	private String productCode;
	@NotNull(message = "Please enter the status")
	private Boolean status;
	@NotBlank(message = "UpdatedBy cannot be null")
	private String updatedBy;
}
