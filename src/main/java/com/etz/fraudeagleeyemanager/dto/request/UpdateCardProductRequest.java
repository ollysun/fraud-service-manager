package com.etz.fraudeagleeyemanager.dto.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateCardProductRequest {

	@NotNull(message="Please enter the cardId")
	private Integer cardId;
	@NotNull(message="Please tell the status")
	private Boolean status;
	@NotNull(message="Please enter the name of the updatedBy")
	private String updatedBy;
	private String productCode;

}
