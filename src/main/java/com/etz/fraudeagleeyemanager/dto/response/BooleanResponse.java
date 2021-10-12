package com.etz.fraudeagleeyemanager.dto.response;

import com.etz.fraudeagleeyemanager.util.RequestUtil;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BooleanResponse {


	private Integer status;
    private String message;
	private double execTime; 
	private String error;

    private Boolean data;

    public BooleanResponse(Object data) {
    	setStatus(200);
        setExecTime((System.nanoTime() - RequestUtil.getStartTime()) / 100000000);
        setMessage(RequestUtil.getMessage());
        setData((Boolean) data);
    }
    
    public BooleanResponse(Object data, String message) {
    	setStatus(200);
    	setExecTime((System.nanoTime() - RequestUtil.getStartTime()) / 100000000);
    	setMessage(message);
    	setData((Boolean) data);
    }
}