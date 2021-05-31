package com.etz.fraudeagleeyemanager.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
@JsonIgnoreProperties({ "productRule", "productEntity", "emailGroup", })
public class RuleProductResponse implements Serializable {

    private Long id;

    private String name;

    private String sourceValueOne;

    private String operatorOne;

    private String compareValueOne;

    private String dataSourceValOne;

    private String logicOperator;

    private String sourceValueTwo;

    private String operatorTwo;

    private String compareValueTwo;

    private String dataSourceValTwo;

    private Integer suspicionLevel;

    private String action;

    private Boolean status;

    private Boolean authorised;

    private String productCode;

    private Long productRuleId;
}
