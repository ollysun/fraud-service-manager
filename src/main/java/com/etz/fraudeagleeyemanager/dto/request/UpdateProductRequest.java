package com.etz.fraudeagleeyemanager.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class UpdateProductRequest extends ProductDetail{
	private String updatedBy;
}
