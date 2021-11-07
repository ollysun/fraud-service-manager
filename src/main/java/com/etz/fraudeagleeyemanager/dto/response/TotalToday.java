package com.etz.fraudeagleeyemanager.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TotalToday {
	private Long total;
	private Long today;
}
