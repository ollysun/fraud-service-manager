package com.etz.fraudeagleeyemanager.dto.request;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class AddAccountRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	@NotBlank(message = "Please enter the account number")
	@PositiveOrZero(message = "Please enter the number")
	@Size(max = 10, min = 10, message="Please enter correct Nuban Number")
	private String accountNo;
	
	@NotBlank(message = "Please enter the account name")
	private String accountName;

	@NotBlank(message = "Please enter the bank code")
	private String bankCode;
	
	@NotBlank(message = "Please enter the bank name")
	private String bankName;

	@JsonIgnore
	private String createdBy;
	
	private String blockReason;
	private Integer suspicionCount;
	
}
