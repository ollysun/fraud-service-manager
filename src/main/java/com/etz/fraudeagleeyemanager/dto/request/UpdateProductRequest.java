package com.etz.fraudeagleeyemanager.dto.request;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class UpdateProductRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	@NotNull(message="Please enter the product Code")
	private String productCode;

	@NotNull(message="Please enter the product Name")
	private String productName;

	private String productDesc;

	@NotNull(message = "Please let know if the user will use card")
	private Boolean useCard;

	@NotNull(message = "Please let know if the user will use Account")
	private Boolean useAccount;

	private String callback;

	@NotNull(message = "Please let know the status ")
	private Boolean status;

	@NotNull(message = "UpdatedBy cannot be empty")
	private String updatedBy;
}
