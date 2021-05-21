package com.etz.fraudeagleeyemanager.entity;

import com.etz.fraudeagleeyemanager.constant.SuspicionLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "rule")
@SQLDelete(sql = "UPDATE rule SET deleted = true WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted = false")
@Data
@ToString(exclude = {"productRule"})
public class Rule extends BaseEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "source_value_1", nullable=false)
	private String sourceValueOne;

	@Column(name = "operator_1", nullable=false)
	private String operatorOne;

	@Column(name = "compare_value_1")
	private Integer compareValueOne;

	@Column(name = "data_source_1", nullable=false)
	private String dataSourceValOne;

	@Column(name = "logic_operator")
	private String logicOperator;

	@Column(name = "source_value_2")
	private String sourceValueTwo;

	@Column(name = "operator_2")
	private String operatorTwo;

	@Column(name = "compare_value_2")
	private Integer compareValueTwo;

	@Column(name = "data_source_2")
	private String dataSourceValTwo;

	@Column(name = "suspicion_level", columnDefinition = "TINYINT", length = 10, nullable=false)
	@Enumerated(EnumType.ORDINAL)
	private SuspicionLevel suspicionLevel;

	@Column(name = "action")
	private String action;
		
	@Column(name = "status", columnDefinition = "TINYINT", length = 1)
	private Boolean status;
	
	@Column(name = "authorised")
	private Boolean authorised;

	@OneToMany(mappedBy = "rule",
			cascade = CascadeType.ALL, orphanRemoval = true)
	@EqualsAndHashCode.Exclude
	private Set<ProductRule> productRule;
		
}
