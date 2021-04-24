package com.etz.fraudeagleeyemanager.dto.response;

import com.etz.fraudeagleeyemanager.util.MetaData;
import com.etz.fraudeagleeyemanager.util.RequestUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@ToString
public class PageResponse<T> {


	private Integer status;
    private String message;
	private double execTime; 
	private String error;

    private List<T> data;

    @JsonProperty("meta_data")
    private MetaData<T> metaData;

    public PageResponse(Page<T> result) {
    	setStatus(200);
        setExecTime((System.nanoTime() - RequestUtil.getStartTime()) / 100000000);
        setMessage(RequestUtil.getMessage());
        setData(result.getContent());
        setMetaData(new MetaData<>(result));
    }
}