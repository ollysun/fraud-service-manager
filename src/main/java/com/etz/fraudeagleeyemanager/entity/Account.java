package com.etz.fraudeagleeyemanager.entity;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;

import org.hibernate.Hibernate;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name = "account",
		uniqueConstraints = @UniqueConstraint(name="UC_ACCOUNT",
				columnNames = {"account_no"}))
@SQLDelete(sql = "UPDATE account SET deleted = true, status=0 WHERE id = ?", check = ResultCheckStyle.COUNT)
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Account extends BaseAuditEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@Column(name = "account_no", unique = true)
	private String accountNo;

	@NotBlank(message = "Account name cannot be empty")
	@Column(name = "account_name", nullable = false)
	private String accountName;

	@NotBlank(message = "Bank code cannot be empty")
	@Column(name = "bank_code", length=200)
	private String bankCode;

	@NotBlank(message = "Bank name cannot be empty")
	@Column(name = "bank_name")
	private String bankName;


	@Column(nullable = false, name = "status", columnDefinition = "TINYINT", length = 1)
	private Boolean status;

	@Column(name = "suspicion_count")
	private Integer suspicionCount;

	@Column(name = "block_reason")
	private String blockReason;


	@JsonManagedReference
	@OneToMany(mappedBy = "account",fetch = FetchType.LAZY,
			cascade = CascadeType.ALL, orphanRemoval = true)
	@ToString.Exclude
	private Set<AccountProduct> accountProducts;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		Account account = (Account) o;

		return Objects.equals(id, account.id);
	}

	@Override
	public int hashCode() {
		return 2083479647;
	}
}
