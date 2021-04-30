package com.etz.fraudeagleeyemanager.dto.request;

import com.etz.fraudeagleeyemanager.constant.Status;
import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AddAccountRequest {

	@NotBlank(message = "Please enter the account number")
	private String accountNo;
	
	@NotBlank(message = "Please enter the account name")
	private String accountName;

	@NotBlank(message = "Please enter the bank code")
	private String bankCode;
	
	@NotBlank(message = "Please enter the bank name")
	private String bankName;

	@NotBlank(message = "Please enter your name")
	private String createdBy;
	
	private String blockReason;
	private Integer suspicion;
	
}
