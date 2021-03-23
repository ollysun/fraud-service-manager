package com.etz.fraudeagleeyemanager.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import java.io.*;
import java.util.Objects;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccountProductKey implements Serializable{

    @Column(name = "product_code")
    private String productCode;

    @Column(name = "account_id")
    private Long accountId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountProductKey)) return false;
        AccountProductKey that = (AccountProductKey) o;
        return productCode.equals(that.productCode) && accountId.equals(that.accountId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productCode, accountId);
    }
}
