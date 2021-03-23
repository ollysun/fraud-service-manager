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
public class CardProductKey implements Serializable {

    @Column(name = "product_code")
    private String productCode;

    @Column(name = "card_id")
    private Long cardId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardProductKey that = (CardProductKey) o;
        return productCode.equals(that.productCode) && cardId.equals(that.cardId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productCode, cardId);
    }
}
