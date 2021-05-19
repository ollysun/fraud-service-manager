package com.etz.fraudeagleeyemanager.entity;

import lombok.*;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "account_product", uniqueConstraints = @UniqueConstraint(name="UC_ACCOUNT_PRODUCT",
        columnNames = {"account_id"}))
@SQLDelete(sql = "UPDATE account_product SET deleted = true WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted = false")
@IdClass(AccountProductId.class)
public class AccountProduct extends BaseEntity implements Serializable {

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
