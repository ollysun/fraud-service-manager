package com.etz.fraudeagleeyemanager.dto.response;

import com.etz.fraudengine.util.RequestUtil;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StringResponse {

	private Integer status;
    private String message;
	private double execTime; 
	private String error;
	
    private String data;

    public StringResponse(String data) {
    	setStatus(200);
        setExecTime((System.nanoTime() - RequestUtil.getStartTime()) / 100000000);
        setMessage(RequestUtil.getMessage());
        this.data = data;
    }
}