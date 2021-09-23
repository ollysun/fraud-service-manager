package com.etz.fraudeagleeyemanager.dto.response;

import com.etz.fraudeagleeyemanager.entity.eagleeyedb.ServiceDataSet;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({ "productEntity" })
public class ServiceDataSetResponse extends ServiceDataSet {
	private static final long serialVersionUID = 1L;
}
