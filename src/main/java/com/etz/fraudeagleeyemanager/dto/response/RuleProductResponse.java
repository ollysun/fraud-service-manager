package com.etz.fraudeagleeyemanager.dto.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({ "productRule", "productEntity", "emailGroup", })
public class RuleProductResponse implements Serializable {
	private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private String sourceValueOne;

    private String valueOneDataType;

    private String operatorOne;

    private String compareValueOne;

    private String dataSourceValOne;

    private String logicOperator;

    private String sourceValueTwo;

    private String valueTwoDataType;

    private String operatorTwo;

    private String compareValueTwo;

    private String dataSourceValTwo;

    private Integer suspicionLevel;

    private String action;

    private Boolean status;

    private Boolean authorised;

    private String serviceId;

    private Long serviceRuleId;
}
