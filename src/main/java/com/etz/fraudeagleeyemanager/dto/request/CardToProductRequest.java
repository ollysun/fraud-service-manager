package com.etz.fraudeagleeyemanager.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class CardToProductRequest {

	private String productCode;
	@JsonAlias("cardID")
	private Integer cardId;
	private Boolean status;
	
}
