package com.etz.fraudeagleeyemanager.entity;

import lombok.*;

import java.io.*;
import java.util.Objects;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductDatasetKey implements Serializable{

    private String productCode;

    private String fieldName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductDatasetKey)) return false;
        ProductDatasetKey that = (ProductDatasetKey) o;
        return productCode.equals(that.productCode) && fieldName.equals(that.fieldName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productCode, fieldName);
    }
}
