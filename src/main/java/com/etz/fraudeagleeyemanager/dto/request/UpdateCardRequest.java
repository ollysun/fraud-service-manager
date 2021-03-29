package com.etz.fraudeagleeyemanager.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UpdateCardRequest {

	@JsonProperty("cardID")
	private Integer cardId;
	private Boolean status;
	private String updatedBy;
	private String updatedAt;
	
}
