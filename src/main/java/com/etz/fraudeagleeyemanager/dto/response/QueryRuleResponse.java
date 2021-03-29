package com.etz.fraudeagleeyemanager.dto.response;

import com.etz.fraudengine.dto.request.CreateRuleRequest;
import lombok.Data;

import java.util.List;

@Data
public class QueryRuleResponse{
	
	private List<CreateRuleRequest> data;
}
