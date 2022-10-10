package com.etz.fraudeagleeyemanager.dto.request;

import lombok.Data;

import java.util.HashMap;

@Data
public class FraudCheckRequest {

	private String transactionID;
	private String productCode;
	private String channel;
	private AddAccountRequest customerDetails;
	private String serviceName;
	private String description;
	private Integer amount;
	private HashMap<String, String> transactionData;

}
