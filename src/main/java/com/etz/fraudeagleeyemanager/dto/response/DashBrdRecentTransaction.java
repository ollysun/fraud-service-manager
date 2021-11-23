package com.etz.fraudeagleeyemanager.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DashBrdRecentTransaction {

	@JsonProperty("transaction_id")
	private String transactionId;
	private String product;
	private String service;
	private String channel;
	private BigDecimal amount;
	
	@JsonProperty("suspicion_level")
	private int suspicionLevel;
	private String action;
	
	@JsonProperty("transaction_date")
	private LocalDateTime transactionDate;
}
