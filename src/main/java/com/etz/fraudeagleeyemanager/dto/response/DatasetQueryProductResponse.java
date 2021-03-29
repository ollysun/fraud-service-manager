package com.etz.fraudeagleeyemanager.dto.response;

import com.etz.fraudengine.dto.request.DatasetProductRequest;
import lombok.Data;

import java.util.List;

@Data
public class DatasetQueryProductResponse {

	private List<DatasetProductRequest> data;
	
}
