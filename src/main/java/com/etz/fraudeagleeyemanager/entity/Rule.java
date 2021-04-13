package com.etz.fraudeagleeyemanager.entity;

import com.etz.fraudeagleeyemanager.constant.Status;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "rule")
@Data
public class Rule extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "source_value_1")
	private String sourceValueOne;

	@Column(name = "operator_1")
	private String operatorOne;

	@Column(name = "compare_value_1")
	private String compareValueOne;

	@Column(name = "data_source_1")
	private String dataSourceOne;

	@Column(name = "logic_operator")
	private String logicOperator;

	@Column(name = "source_value_2")
	private String sourceValueTwo;

	@Column(name = "operator_2")
	private String operatorTwo;

	@Column(name = "compare_value_2")
	private String compareValueTwo;

	@Column(name = "data_source_2")
	private String dataSourceTwo;

	@Column(name = "suspicion_level")
	private Integer suspicionLevel;

	@Column(name = "action")
	private String action;
		
	@Column(name = "status")
	@Enumerated(EnumType.ORDINAL)
	private Status status;
	
	@Column(name = "authorised")
	private Boolean authorised;

		
}
