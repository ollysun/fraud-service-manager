package com.etz.fraudeagleeyemanager.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class QueryProductResponse {

	private List<ProductMetaData> data;
}
