package com.etz.fraudeagleeyemanager.entity;

import com.etz.fraudeagleeyemanager.constant.DataSource;
import com.etz.fraudeagleeyemanager.constant.LogicOperator;
import com.etz.fraudeagleeyemanager.constant.Status;
import com.etz.fraudeagleeyemanager.constant.SuspicionLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "rule")
@Data
public class Rule extends BaseEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "source_value_1")
	private String sourceValueOne;

	@Column(name = "operator_1")
	private String operatorOne;

	@Column(name = "compare_value_1")
	private Integer compareValueOne;

	@Column(name = "data_source_1")
	@Enumerated(EnumType.STRING)
	private DataSource dataSourceOne;

	@Column(name = "logic_operator")
	@Enumerated(EnumType.STRING)
	private LogicOperator logicOperator;

	@Column(name = "source_value_2")
	private String sourceValueTwo;

	@Column(name = "operator_2")
	private String operatorTwo;

	@Column(name = "compare_value_2")
	private Integer compareValueTwo;

	@Column(name = "data_source_2")
	@Enumerated(EnumType.STRING)
	private DataSource dataSourceTwo;

	@Column(name = "suspicion_level", columnDefinition = "INT", length = 10)
	@Enumerated(EnumType.ORDINAL)
	private SuspicionLevel suspicionLevel;

	@Column(name = "action")
	private String action;
		
	@Column(name = "status", columnDefinition = "TINYINT", length = 1)
	private Boolean status;
	
	@Column(name = "authorised")
	private Boolean authorised;

	@OneToMany(mappedBy = "rule", cascade = CascadeType.REMOVE)
	private Set<ProductRule> productRules;
		
}
