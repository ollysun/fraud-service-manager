package com.etz.fraudeagleeyemanager.dto.request;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DashboardRequest implements Serializable{

	private static final long serialVersionUID = 1L;
	private String start;
	private String end;
	private String productCode;
	//private int limit;
}
