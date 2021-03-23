package com.etz.fraudeagleeyemanager.entity;

import com.etz.fraudeagleeyemanager.constant.Status;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "account", 
		uniqueConstraints = @UniqueConstraint(
				columnNames = {"account_no", "bank_code"}))
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Account implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Account number cannot be empty")
	@Column(name = "account_no")
	private String accountNo;

	@NotBlank(message = "Account name cannot be empty")
	@Column(name = "account_name", nullable = false)
	private String accountName;

	@NotBlank(message = "Bank code cannot be empty")
	@Column(name = "bank_code")
	private String bankCode;

	@NotBlank(message = "Bank name cannot be empty")
	@Column(name = "bank_name")
	private String bankName;
	
	@Column(nullable = false, name = "status", columnDefinition = "TINYINT", length = 1)
	@Enumerated(EnumType.ORDINAL)
	private Status status;
	
	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	@Column(name = "suspicion_count")
	private Integer suspicionCount;

	@Column(name = "block_reason")
	private String blockReason;

	@ToString.Exclude
	@OneToMany(mappedBy = "account")
	Set<AccountProduct> accountProducts = new HashSet<>();

	@ToString.Exclude
	@OneToMany(mappedBy = "account", fetch = FetchType.LAZY,
			cascade = CascadeType.ALL)
	Set<TransactionLog> transactionLog = new HashSet<>();

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Account that = (Account) o;

		return id != null && id.equals(that.id);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
