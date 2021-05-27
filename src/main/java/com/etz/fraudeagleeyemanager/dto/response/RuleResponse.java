package com.etz.fraudeagleeyemanager.dto.response;

import com.etz.fraudeagleeyemanager.entity.Rule;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({ "productRule"})
public class RuleResponse extends Rule {
}
