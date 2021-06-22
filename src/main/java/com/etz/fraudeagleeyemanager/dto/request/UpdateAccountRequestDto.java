package com.etz.fraudeagleeyemanager.dto.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class UpdateAccountRequestDto implements Serializable {
	private static final long serialVersionUID = 1L;
    private String accountNumber;
    private Integer count;
    private Boolean status;
    private String blockReason;
}
