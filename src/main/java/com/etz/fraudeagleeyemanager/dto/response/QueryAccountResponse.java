package com.etz.fraudeagleeyemanager.dto.response;

import com.etz.fraudengine.dto.request.AddAccountRequest;
import lombok.Data;

import java.util.List;

@Data
public class QueryAccountResponse{

	private List<AddAccountRequest> data;
	
}
