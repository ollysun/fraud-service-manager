package com.etz.fraudeagleeyemanager.entity;

import lombok.*;

import java.io.Serializable;


@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ProductRuleId implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long serviceRuleId;

    private String serviceId;
}
