package com.etz.fraudeagleeyemanager.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class CreateParameterRequest {

	@JsonAlias("paramID")
	private Integer paramId;
	private String name;
	private String operator;
	private Boolean requireValue;
	private String createdBy;
	private String createdAt;
	private String updatedBy;
	private String updatedAt;
	private Boolean authorised;
	
}
