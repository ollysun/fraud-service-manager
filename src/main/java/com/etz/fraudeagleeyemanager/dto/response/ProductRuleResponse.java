package com.etz.fraudeagleeyemanager.dto.response;

import com.etz.fraudeagleeyemanager.entity.ProductRule;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({ "productEntity", "emailGroup"})
public class ProductRuleResponse extends ProductRule {
}
