package com.etz.fraudeagleeyemanager.entity;

import lombok.*;

import java.io.*;
import java.util.Objects;

@Data
public class ProductId implements Serializable {

    private String code;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductId)) return false;
        ProductId productId = (ProductId) o;
        return code.equals(productId.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
}
