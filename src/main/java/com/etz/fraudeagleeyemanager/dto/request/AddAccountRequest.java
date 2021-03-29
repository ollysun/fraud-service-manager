package com.etz.fraudeagleeyemanager.dto.request;

import com.etz.fraudeagleeyemanager.constant.Status;
import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AddAccountRequest {

	@NotBlank(message = "account.accountNo.not-blank")
	private String accountNo;
	
	@NotBlank(message = "account.accountName.not-blank")
	private String accountName;

	@NotBlank(message = "account.bankCode.not-blank")
	private String bankCode;
	
	@NotBlank(message = "account.bankName.not-blank")
	private String bankName;

	@NotBlank(message = "account.status.not-blank")
	private Status status;
	
	@JsonAlias("accountID")
	private Integer accountId;
	private Integer suspicion;
	
}
