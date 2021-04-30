package com.etz.fraudeagleeyemanager.dto.request;

import lombok.Data;
import javax.validation.constraints.NotNull;

@Data
public class CardToProductRequest {
	@NotNull(message = "please enter the productCode")
	private String productCode;
	@NotNull(message="Please enter the cardId")
	private Integer cardId;
	@NotNull(message="CreatedBy cannot be null")
	private String createdBy;
}
