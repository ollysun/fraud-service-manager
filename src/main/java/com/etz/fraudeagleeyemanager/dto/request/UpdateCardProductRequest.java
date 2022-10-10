package com.etz.fraudeagleeyemanager.dto.request;

import lombok.Data;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

@Data
public class UpdateCardProductRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	@NotNull(message="Please enter the cardId")
	private Long cardId;
	@NotNull(message="Please tell the status")
	private Boolean status;
	@NotNull(message="Please enter the name of the updatedBy")
	private String updatedBy;
	private String productCode;

}
