package com.etz.fraudeagleeyemanager.service;

import com.etz.fraudengine.dto.request.FraudCheckRequest;
import com.etz.fraudengine.dto.response.FraudCheckResponse;
import org.springframework.stereotype.Service;

@Service
public class FraudService {

	
	public FraudCheckResponse fraudCheck(FraudCheckRequest request) {
		
		// logic
		
		FraudCheckResponse fraudCheckResponse = new FraudCheckResponse();
		fraudCheckResponse.setAction("");
		fraudCheckResponse.setNotifyCustomer(false);
		fraudCheckResponse.setNotifyMessage("");
		
		return fraudCheckResponse;
	}
}
