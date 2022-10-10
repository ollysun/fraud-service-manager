package com.etz.fraudeagleeyemanager.dto.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RoleIdByPermissionResponse {

	private Integer status;
    private String message;
	private double execTime; 
	private String error;

    private List<Long> data;
    
}
