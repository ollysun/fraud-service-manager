package com.etz.fraudeagleeyemanager.entity;

import com.etz.fraudeagleeyemanager.constant.SuspicionLevel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "rule",uniqueConstraints = @UniqueConstraint(name="UQ_RULE",
		columnNames = {"rule_name"}))
@SQLDelete(sql = "UPDATE rule SET deleted = true WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted = false")
@Getter
@Setter
@EqualsAndHashCode(exclude = {"productRule"}, callSuper = false)
public class Rule extends BaseEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "rule_name", nullable=false)
	private String name;
	
	@Column(name = "source_value_1", nullable=false)
	private String sourceValueOne;

	@Column(name = "operator_1", nullable=false)
	private String operatorOne;

	@Column(name = "compare_value_1")
	private String compareValueOne;

	@Column(name = "data_source_1", nullable=false)
	private String dataSourceValOne;

	@Column(name = "logic_operator")
	private String logicOperator;

	@Column(name = "source_value_2")
	private String sourceValueTwo;

	@Column(name = "operator_2")
	private String operatorTwo;

	@Column(name = "compare_value_2")
	private String compareValueTwo;

	@Column(name = "data_source_2")
	private String dataSourceValTwo;

	private Integer suspicionLevel;

	@Column(name = "action")
	private String action;
		
	@Column(name = "status", columnDefinition = "TINYINT", length = 1)
	private Boolean status;
	
	@Column(name = "authorised")
	private Boolean authorised;

	@JsonManagedReference
	@OneToMany(mappedBy = "rule",fetch = FetchType.LAZY,
			cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<ProductRule> productRule;
		
}
