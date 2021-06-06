package com.etz.fraudeagleeyemanager.entity;

import lombok.*;

import java.io.Serializable;


@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ProductRuleId implements Serializable {

    private Long id;

    private Long ruleId;

    private String productCode;
}
