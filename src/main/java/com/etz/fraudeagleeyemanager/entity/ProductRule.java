package com.etz.fraudeagleeyemanager.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "product_rule")
@SQLDelete(sql = "UPDATE product_rule SET deleted = true WHERE rule_id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted = false")
@Getter
@Setter
@EqualsAndHashCode(exclude = {"productEntity", "emailGroup", "rule"}, callSuper = false)
@RequiredArgsConstructor
@ToString
@IdClass(ProductRuleId.class)
public class ProductRule extends BaseAuditVersionEntity<ProductRuleId> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "rule_id")
	private Long ruleId;

	@Id
	@Column(name = "product_code",  columnDefinition="VARCHAR(100)")
	private String productCode;

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
	@JoinColumn(name = "email_group_id", foreignKey = @ForeignKey(name = "FK_PRODUCT_RULE_EMAIL_GROUP_ID"),
			 insertable = false, updatable = false)
	private EmailGroup emailGroup;


	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("ruleId")
	@JoinColumn(name = "rule_id")
	private Rule rule;


	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("productCode")
	@JoinColumn(name = "product_code")
	private ProductEntity productEntity;


	@Override
	public ProductRuleId getId() {
		return new ProductRuleId(ruleId, productCode);
	}


}
