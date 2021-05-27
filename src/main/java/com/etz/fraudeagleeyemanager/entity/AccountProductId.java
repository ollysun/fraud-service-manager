package com.etz.fraudeagleeyemanager.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Embeddable
public class AccountProductId implements Serializable {

    @NotNull
    @Column(name = "product_code", nullable = false, columnDefinition="VARCHAR(100)")
    private String productCode;

    @NotNull
    @Column(name = "account_id", nullable = false, columnDefinition = "bigint")
    private Long accountId;
}
