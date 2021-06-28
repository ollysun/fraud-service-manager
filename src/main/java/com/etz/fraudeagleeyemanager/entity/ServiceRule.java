package com.etz.fraudeagleeyemanager.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "service_rule")
@SQLDelete(sql = "UPDATE service_rule SET deleted = true, status=0 WHERE rule_id = ?", check = ResultCheckStyle.COUNT)
@Getter
@Setter
@AllArgsConstructor
@ToString
@IdClass(ProductRuleId.class)
@NoArgsConstructor
public class ServiceRule extends BaseAuditVersionEntity<ProductRuleId> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "rule_id")
	private Long ruleId;

	@Id
	@Column(name = "service_id")
	private Long serviceId;

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

	@JsonBackReference
	@OneToOne(fetch = FetchType.LAZY)
	@MapsId("emailGroupId")
	@JoinColumn(name = "email_group_id", foreignKey = @ForeignKey(name = "FK_PRODUCT_RULE_EMAIL_GROUP_ID"))
	@ToString.Exclude
	private EmailGroup emailGroup;


	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("ruleId")
	@JoinColumn(name = "rule_id")
	@ToString.Exclude
	private Rule rule;

	@Override
	public ProductRuleId getId() {
		return new ProductRuleId(ruleId, serviceId);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		ServiceRule that = (ServiceRule) o;

		if (!Objects.equals(ruleId, that.ruleId)) return false;
		return Objects.equals(serviceId, that.serviceId);
	}

	@Override
	public int hashCode() {
		int result = Objects.hashCode(ruleId);
		result = 31 * result + (Objects.hashCode(serviceId));
		return result;
	}
}
