package com.etz.fraudeagleeyemanager.dto.request;

import com.etz.fraudeagleeyemanager.constant.Status;
import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class UpdateAccountRequest {
	
	@JsonAlias("accountID")
	private Integer accountId;
	private Status status;
	private String updatedBy;
	private String updatedAt;	
}
