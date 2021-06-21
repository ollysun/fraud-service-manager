package com.etz.fraudeagleeyemanager.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "account_product", uniqueConstraints = @UniqueConstraint(name="UC_ACCOUNT_PRODUCT",
        columnNames = {"account_id"}))
@SQLDelete(sql = "UPDATE account_product SET deleted = true, status=0 WHERE id = ? AND version = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted = false")
@IdClass(AccountProductId.class)
public class AccountProduct extends BaseAuditVersionEntity<AccountProductId> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
    @Column(name = "product_code", nullable = false, columnDefinition="VARCHAR(100)")
    private String productCode;

    @Id
    @Column(name = "account_id")
    private Long accountId;

    @Column(nullable = false, name = "status", columnDefinition = "TINYINT", length = 1)
    private Boolean status;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("accountId")
    @JoinColumn(name = "account_id")
    private Account account;


    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productCode")
    @JoinColumn(name = "product_code")
    private ProductEntity productEntity;


	@Override
	public AccountProductId getId() {
		return new AccountProductId(getProductCode(), getAccountId());
	}

}
