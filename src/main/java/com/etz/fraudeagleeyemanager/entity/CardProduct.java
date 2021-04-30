package com.etz.fraudeagleeyemanager.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "card_product", uniqueConstraints = @UniqueConstraint(name="UC_CARD_PRODUCT",
        columnNames = {"card_id"}))
@Entity
@IdClass(CardProductId.class)
public class CardProduct extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Id
    @Column(name = "product_code", nullable = false, columnDefinition="VARCHAR(100)")
    private String productCode;

    @Id
    @Column(name = "card_id", nullable = false,  columnDefinition = "bigint")
    private Long cardId;

    @ManyToOne
    @MapsId("productCode")
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_CARD_PRODUCT_CODE"),
            name = "product_code",
            referencedColumnName="code")
    private ProductEntity productEntity;

    @ManyToOne
    @MapsId("cardId")
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_CARD_PRODUCT_ID"),
            name = "card_id",
            referencedColumnName="id")
    private Card card;

    @Column(nullable = false, name = "status", columnDefinition = "TINYINT", length = 1)
    private Boolean status;

}
