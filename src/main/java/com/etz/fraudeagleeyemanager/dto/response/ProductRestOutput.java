package com.etz.fraudeagleeyemanager.dto.response;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class ProductRestOutput implements Serializable {
	private static final long serialVersionUID = 1L;
    private String status;
    private String message;
    private List<ContentResponse> data;
}
