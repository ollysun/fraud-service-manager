package com.etz.fraudeagleeyemanager.dto.request;


import lombok.Data;
import javax.validation.constraints.NotNull;

@Data
public class AccountToProductRequest {

	@NotNull(message="Please enter the product Coe")
	private String productCode;
	@NotNull(message="Please enter the accountId")
	private Long accountId;
	@NotNull(message="CratedBY cannot be empty")
	private String createdBy;
	
}
