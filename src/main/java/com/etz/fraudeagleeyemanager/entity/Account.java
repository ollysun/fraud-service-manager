package com.etz.fraudeagleeyemanager.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Set;


@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "account", 
		uniqueConstraints = @UniqueConstraint(name="UC_ACCOUNT",
				columnNames = {"account_no", "bank_code"}))
@Data
public class Account extends BaseEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "account_no", unique = true)
	private Long accountNo;

	@NotBlank(message = "Account name cannot be empty")
	@Column(name = "account_name", nullable = false)
	private String accountName;

	@NotBlank(message = "Bank code cannot be empty")
	@Column(name = "bank_code", unique = true, length=200)
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

	@OneToMany(mappedBy = "account")
	private Set<AccountProduct> accounts;

}
