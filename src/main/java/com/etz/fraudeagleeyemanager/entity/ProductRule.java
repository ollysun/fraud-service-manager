package com.etz.fraudeagleeyemanager.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Entity
@Table(name = "product_rule")
@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class ProductRule extends BaseEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Rule Id cannot be empty")
	@Column(name = "rule_id")
	private Long ruleId;

	@Column(name = "product_code",  columnDefinition="VARCHAR(100)")
	private String productCode;

	@NotBlank(message = "email_group_id cannot be empty")
	@Column(name = "email_group_id")
	private Long emailGroupId;
	
	@Column(name = "notify_admin", columnDefinition = "TINYINT", length = 1)
	private Boolean notifyAdmin;
			
	@Column(nullable = false, name = "notify_customer", columnDefinition = "TINYINT", length = 1)
	private Boolean notifyCustomer;
	
	@Column(nullable = false, name = "status", columnDefinition = "TINYINT", length = 1)
	private Boolean status;
	
	@Column(nullable = false, name = "authorised", columnDefinition = "TINYINT", length = 1)
	private Boolean authorised;

	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "product_code", foreignKey = @ForeignKey(name = "FK_PRODUCT_RULE_CODE"),
			referencedColumnName="code", insertable = false, updatable = false)
	private ProductEntity productEntity;

	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "email_group_id", foreignKey = @ForeignKey(name = "FK_PRODUCT_RULE_EMAIL_GROUP_ID"),
			referencedColumnName="id", insertable = false, updatable = false)
	private EmailGroup emailGroup;

	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "rule_id", foreignKey = @ForeignKey(name = "FK_PRODUCT_RULE_RULE_ID"),
			referencedColumnName="id", insertable = false, updatable = false)
	private Rule rule;


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
