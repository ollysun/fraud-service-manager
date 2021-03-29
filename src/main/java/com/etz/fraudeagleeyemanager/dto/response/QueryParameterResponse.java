package com.etz.fraudeagleeyemanager.dto.response;

import com.etz.fraudengine.dto.request.CreateParameterRequest;
import lombok.Data;

import java.util.List;

@Data
public class QueryParameterResponse {

	private List<CreateParameterRequest> data;
}
