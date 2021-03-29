package com.etz.fraudeagleeyemanager.dto.response;

import com.etz.fraudengine.dto.request.CardRequest;
import lombok.Data;

import java.util.List;

@Data
public class QueryCardResponse {
	
	private List<CardRequest> data;
}
