package com.etz.fraudeagleeyemanager.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@ToString
@AllArgsConstructor
@Entity
@Table(name = "account_product", uniqueConstraints = @UniqueConstraint(name="UC_ACCOUNT_PRODUCT",
        columnNames = {"account_id"}))
@SQLDelete(sql = "UPDATE account_product SET deleted = true, status=0 WHERE id = ?", check = ResultCheckStyle.COUNT)
@IdClass(AccountProductId.class)
@NoArgsConstructor
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
    @ToString.Exclude
    private Account account;


    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productCode")
    @JoinColumn(name = "product_code")
    @ToString.Exclude
    private ProductEntity productEntity;


	@Override
	public AccountProductId getId() {
		return new AccountProductId(getProductCode(), getAccountId());
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AccountProduct that = (AccountProduct) o;

        if (!Objects.equals(productCode, that.productCode)) return false;
        return Objects.equals(accountId, that.accountId);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(productCode);
        result = 31 * result + (Objects.hashCode(accountId));
        return result;
    }
}
