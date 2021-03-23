package com.etz.fraudeagleeyemanager.entity;

import com.etz.fraudeagleeyemanager.constant.BooleanStatus;
import com.etz.fraudeagleeyemanager.constant.Status;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "product_rule", 
uniqueConstraints = @UniqueConstraint(
		columnNames = {"rule_id", "product_code"}))
@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class ProductRule extends BaseEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@NotBlank(message = "Rule Id cannot be empty")
	@Column(name = "rule_id")
	private Long ruleId;
	
	@Column(name = "notify_admin", columnDefinition = "TINYINT", length = 1)
	@Enumerated(EnumType.ORDINAL)
	private BooleanStatus notifyAdmin;
			
	@Column(nullable = false, name = "notify_customer", columnDefinition = "TINYINT", length = 1)
	@Enumerated(EnumType.ORDINAL)
	private BooleanStatus notifyCustomer;
	
	@Column(nullable = false, name = "status", columnDefinition = "TINYINT", length = 1)
	@Enumerated(EnumType.ORDINAL)
	private Status status;
	
	@Column(nullable = false, name = "authorised", columnDefinition = "TINYINT", length = 1)
	@Enumerated(EnumType.ORDINAL)
	private Status authorised;

	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "product_code", nullable = false)
	private Product product;

	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "email_group")
	private EmailGroup emailGroup;

	@ToString.Exclude
	@OneToMany(mappedBy = "productRule", fetch = FetchType.LAZY,
			cascade = CascadeType.ALL)
	Set<TransactionLog> transactionLog = new HashSet<>();

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ProductRule that = (ProductRule) o;

		return id != null && id.equals(that.id);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
