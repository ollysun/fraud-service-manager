package com.etz.fraudeagleeyemanager.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
public class FraudCheckResponse extends StringResponse{

	private String action;
	private Boolean notifyCustomer;
	private String notifyMessage;
	
	FraudCheckResponse(String data){ 
		super(data);
	}
}
