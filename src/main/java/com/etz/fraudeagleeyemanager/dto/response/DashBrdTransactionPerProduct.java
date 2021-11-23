package com.etz.fraudeagleeyemanager.dto.response;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DashBrdTransactionPerProduct {
	
	@JsonIgnore
	private String productCode;
	
	private String name;
	
	@JsonProperty("total_transaction")
	private BigDecimal totalTransaction;
	
	@JsonProperty("transaction_count")
	private long transactionCount;
	
	@JsonProperty("total_flagged")
	private long totalFlagged;
	
	@JsonProperty("total_unflagged")
	private long totalUnflagged;
}
