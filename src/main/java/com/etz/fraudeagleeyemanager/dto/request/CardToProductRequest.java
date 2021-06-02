package com.etz.fraudeagleeyemanager.dto.request;

import lombok.Data;
import org.slf4j.Logger;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CardToProductRequest {
	@NotBlank(message = "please enter the productCode")
	private String productCode;
	@NotNull(message="Please enter the cardId")
	private Long cardId;
	@NotBlank(message="CreatedBy cannot be null")
	private String createdBy;
}
