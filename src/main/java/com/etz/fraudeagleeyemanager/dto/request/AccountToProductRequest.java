package com.etz.fraudeagleeyemanager.dto.request;


import lombok.Data;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

@Data
public class AccountToProductRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	@NotNull(message="Please enter the product Code")
	private String productCode;
	@NotNull(message="Please enter the accountId")
	private Long accountId;
	@NotNull(message="CratedBY cannot be empty")
	private String createdBy;
	
}
