package com.etz.fraudeagleeyemanager.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "account_product", uniqueConstraints = @UniqueConstraint(name="UC_ACCOUNT_PRODUCT",
        columnNames = {"account_id"}))
@SQLDelete(sql = "UPDATE account_product SET deleted = true WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted = false")
//@IdClass(AccountProductId.class)
public class AccountProduct extends BaseAuditEntity implements Serializable {

//    @Id
//    @Column(name = "product_code", nullable = false, columnDefinition="VARCHAR(100)")
//    private String productCode;
//
//    @Id
//    @Column(name = "account_id", nullable = false, columnDefinition = "bigint")
//    private Long accountId;

    @EmbeddedId
    private AccountProductId accountProductId;

//    @ManyToOne
//    @MapsId("productCode")
//    @JoinColumn(name = "product_code")
//    private ProductEntity productEntity;
//
//    @ManyToOne
//    @MapsId("accountId")
//    @JoinColumn(name = "account_id")
//    private Account account;

    @Column(nullable = false, name = "status", columnDefinition = "TINYINT", length = 1)
    private Boolean status;


}
