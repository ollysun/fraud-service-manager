package com.etz.fraudeagleeyemanager.dto.request;

import com.etz.fraudeagleeyemanager.constant.BooleanStatus;
import com.etz.fraudeagleeyemanager.constant.Status;
import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class DatasetProductRequest {

	private String productCode;
	private String fieldName;
	private String dataType;
	private BooleanStatus mandatory;
	
	@JsonAlias(value = "updatedBy")
	private String createdBy;
	private Status authorised;
	
}
