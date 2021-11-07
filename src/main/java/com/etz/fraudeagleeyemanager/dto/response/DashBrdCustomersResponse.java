package com.etz.fraudeagleeyemanager.dto.response;

import lombok.Data;

@Data
public class DashBrdCustomersResponse {
	private TotalToday card;
	private TotalToday account;
}
