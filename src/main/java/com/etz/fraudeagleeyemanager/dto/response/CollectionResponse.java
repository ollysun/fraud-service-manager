package com.etz.fraudeagleeyemanager.dto.response;

import com.etz.fraudeagleeyemanager.util.RequestUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Collection;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CollectionResponse<T> {


	private Integer status;
    private String message;
	private double execTime; 
	private String error;

    private Collection<T> data;

    public CollectionResponse(Collection<T> result) {
    	setStatus(200);
        setExecTime((System.nanoTime() - RequestUtil.getStartTime()) / 100000000);
        setMessage(RequestUtil.getMessage());
        setData(result);
    }
    
    public CollectionResponse(Collection<T> result, String message) {
    	setStatus(200);
        setExecTime((System.nanoTime() - RequestUtil.getStartTime()) / 100000000);
        setMessage(message);
        setData(result);
    }
}