package com.etz.fraudeagleeyemanager.dto.response;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DashBrdTransactionPerProduct {
	
	private String name;
	
	@JsonProperty("total_transaction")
	private BigDecimal totalTransaction;
	
	@JsonProperty("transaction_count")
	private int transactionCount;
	
	@JsonProperty("total_flagged")
	private int totalFlagged;
	
	@JsonProperty("total_unflagged")
	private int totalUnflagged;
}
