package com.etz.fraudeagleeyemanager.dto.response;

import com.etz.fraudeagleeyemanager.entity.ProductRule;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({ "productEntity", "emailGroup", "rule" })
public class ProductRuleResponse extends ProductRule {
}
