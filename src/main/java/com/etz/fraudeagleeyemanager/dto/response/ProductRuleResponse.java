package com.etz.fraudeagleeyemanager.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties({ "productEntity", "emailGroup", "rule"})
public class ProductRuleResponse {

    private Long id;

    private Long ruleId;

    private String productCode;

    private Long emailGroupId;

    private Boolean notifyAdmin;

    private Boolean notifyCustomer;

    private Boolean status;

    private Boolean authorised;
}
