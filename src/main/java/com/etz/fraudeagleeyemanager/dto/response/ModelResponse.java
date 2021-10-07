package com.etz.fraudeagleeyemanager.dto.response;

import org.springframework.http.HttpStatus;

import com.etz.fraudeagleeyemanager.util.RequestUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ModelResponse<T> {


	private Integer status;
    private String message;
	private double execTime; 
	private String error;

    private T data;

    public ModelResponse(T data) {
    	setStatus(200);
    	setExecTime((System.nanoTime() - RequestUtil.getStartTime()) / 100000000);
        setMessage(RequestUtil.getMessage());
        setData(data);
    }
    
    public ModelResponse(T data, HttpStatus httpStatus) {
    	setStatus(httpStatus.value());
    	setExecTime((System.nanoTime() - RequestUtil.getStartTime()) / 100000000);
        setMessage(RequestUtil.getMessage());
        setData(data);
    }
}