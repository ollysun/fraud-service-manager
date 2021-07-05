package com.etz.fraudeagleeyemanager.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProductServiceResponse {

    private String serviceId;

    private String serviceName;

    private String productCode;

    private String description;

    private String callbackUrl;

    private Boolean status;
}
