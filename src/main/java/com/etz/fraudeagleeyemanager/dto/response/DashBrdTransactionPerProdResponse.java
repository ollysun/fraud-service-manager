package com.etz.fraudeagleeyemanager.dto.response;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class DashBrdTransactionPerProdResponse {

	private int status;
	private long totalFlagged;
	private List<DashBrdTransactionPerProduct> data = new ArrayList<>();
}
