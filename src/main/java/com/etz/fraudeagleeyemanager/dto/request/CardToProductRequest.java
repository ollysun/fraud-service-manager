package com.etz.fraudeagleeyemanager.dto.request;

import lombok.Data;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CardToProductRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	@NotBlank(message = "please enter the productCode")
	private String productCode;
	@NotNull(message="Please enter the cardId")
	private Long cardId;
	@NotBlank(message="CreatedBy cannot be null")
	private String createdBy;
}
