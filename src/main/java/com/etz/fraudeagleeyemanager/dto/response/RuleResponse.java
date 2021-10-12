package com.etz.fraudeagleeyemanager.dto.response;

import com.etz.fraudeagleeyemanager.entity.Rule;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
@JsonIgnoreProperties({ "productRule"})
public class RuleResponse extends Rule {
	private static final long serialVersionUID = 1L;

}
