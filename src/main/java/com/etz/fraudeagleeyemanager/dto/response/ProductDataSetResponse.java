package com.etz.fraudeagleeyemanager.dto.response;

import com.etz.fraudeagleeyemanager.entity.ProductDataSet;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({ "productEntity" })
public class ProductDataSetResponse extends ProductDataSet {
	private static final long serialVersionUID = 1L;
}
