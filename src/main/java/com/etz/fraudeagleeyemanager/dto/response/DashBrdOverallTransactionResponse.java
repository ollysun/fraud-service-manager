package com.etz.fraudeagleeyemanager.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DashBrdOverallTransactionResponse {

	@JsonProperty("transaction_value")
	private TotalToday transactionValue;
	
	@JsonProperty("transaction_count")
	private TotalToday transactionCount;
}
