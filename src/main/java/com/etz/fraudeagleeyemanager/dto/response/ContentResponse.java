package com.etz.fraudeagleeyemanager.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContentResponse {
     private Long id;
     private String productName;
     private String description;
     private String productManager;
     private String productCode;
}
