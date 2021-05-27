package com.etz.fraudeagleeyemanager.dto.request;

import lombok.Data;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

@Data
public class CardToProductRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	@NotNull(message = "please enter the productCode")
	private String productCode;
	@NotNull(message="Please enter the cardId")
	private Integer cardId;
	@NotNull(message="CreatedBy cannot be null")
	private String createdBy;
}
