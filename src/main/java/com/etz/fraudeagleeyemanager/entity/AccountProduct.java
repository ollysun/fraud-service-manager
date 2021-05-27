package com.etz.fraudeagleeyemanager.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "account_product", uniqueConstraints = @UniqueConstraint(name="UC_ACCOUNT_PRODUCT",
        columnNames = {"account_id"}))
@IdClass(AccountProductId.class)
public class AccountProduct extends BaseAuditEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_code", nullable = false, columnDefinition="VARCHAR(100)")
    private String productCode;

    @Column(name = "account_id", nullable = false, columnDefinition = "bigint")
    private Long accountId;

    @ManyToOne
    @MapsId("productCode")
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_ACCOUNT_PRODUCT_PRODUCT_CODE"),
            name = "product_code",
            referencedColumnName="code")
    private ProductEntity productEntity;

    @ManyToOne
    @MapsId("accountId")
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_ACCOUNT_PRODUCT_CARD_ID"),
            name = "account_id",
            referencedColumnName="id")
    private Account account;

    @Column(nullable = false, name = "status", columnDefinition = "TINYINT", length = 1)
    private Boolean status;


}
