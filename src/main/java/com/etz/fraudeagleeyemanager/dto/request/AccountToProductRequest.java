package com.etz.fraudeagleeyemanager.dto.request;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AccountToProductRequest implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@NotBlank(message="Please enter the product Code")
	private String productCode;
	@NotNull(message="Please enter the accountId")
	private Long accountId;
	@JsonIgnore
	private String createdBy;
	
}
