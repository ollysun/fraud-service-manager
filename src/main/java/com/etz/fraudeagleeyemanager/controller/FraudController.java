package com.etz.fraudeagleeyemanager.controller;

import com.etz.fraudengine.dto.request.FraudCheckRequest;
import com.etz.fraudengine.dto.response.FraudCheckResponse;
import com.etz.fraudengine.dto.response.ModelResponse;
import com.etz.fraudengine.service.FraudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fraud")
public class FraudController {

	@Autowired
	FraudService fraudService;
	
	
	@PostMapping(path = "/check")
	public ModelResponse<FraudCheckResponse> checkForFraud(@RequestBody FraudCheckRequest request){
		return new ModelResponse<FraudCheckResponse>(fraudService.fraudCheck(request));
	}
	
}
