package com.etz.fraudeagleeyemanager.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "card_product", uniqueConstraints = @UniqueConstraint(name="UC_CARD_PRODUCT",
        columnNames = {"card_id"}))
@SQLDelete(sql = "UPDATE card_product SET deleted = true WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted = false")
@IdClass(CardProductId.class)
public class CardProduct extends BaseAuditEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Id
    @Column(name = "product_code", nullable = false, columnDefinition="VARCHAR(100)")
    private String productCode;

    @Id
    @Column(name = "card_id", nullable = false,  columnDefinition = "bigint")
    private Long cardId;

    @JsonBackReference
    @ManyToOne
    @MapsId("productCode")
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_CARD_PRODUCT_CODE"),
            name = "product_code",
            referencedColumnName="code")
    private ProductEntity productEntity;

    @JsonBackReference
    @ManyToOne
    @MapsId("cardId")
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_CARD_PRODUCT_ID"),
            name = "card_id",
            referencedColumnName="id")
    private Card card;

    @Column(nullable = false, name = "status", columnDefinition = "TINYINT", length = 1)
    private Boolean status;

}
