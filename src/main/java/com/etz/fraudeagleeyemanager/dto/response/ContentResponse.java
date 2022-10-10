package com.etz.fraudeagleeyemanager.dto.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContentResponse implements Serializable{
	private static final long serialVersionUID = 1L;
     private Long id;
     private String productName;
     private String description;
     private String productManager;
     private String productCode;
}
