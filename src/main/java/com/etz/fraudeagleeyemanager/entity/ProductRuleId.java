package com.etz.fraudeagleeyemanager.entity;

import lombok.*;

import java.io.Serializable;


@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ProductRuleId implements Serializable {

    private Long ruleId;

    private String productCode;
}
