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
//@SQLDelete(sql = "UPDATE product_rule SET deleted = true WHERE rule_id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted = false")
@Getter
@Setter
@EqualsAndHashCode(exclude = {"productEntity", "emailGroup", "rule"}, callSuper = false)
@RequiredArgsConstructor
@ToString
public class ProductRule extends BaseEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "rule_id")
	private Long ruleId;

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
	@ManyToOne(fetch = FetchType.LAZY)
	private ProductEntity productEntity;

	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "email_group_id", foreignKey = @ForeignKey(name = "FK_PRODUCT_RULE_EMAIL_GROUP_ID"),
			referencedColumnName="id", insertable = false, updatable = false)
	private EmailGroup emailGroup;

	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "rule_id", insertable = false, updatable = false)
	private Rule rule;



}
