package com.etz.fraudeagleeyemanager.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.hibernate.Hibernate;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "rule")
@SQLDelete(sql = "UPDATE rule SET deleted = true, status=0 WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted=false")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Rule extends BaseAuditEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "rule_name")
	private String name;
	
	@Column(name = "source_value_1", nullable=false)
	private String sourceValueOne;

	@Column(name = "value_1_data_type", nullable=false)
	private String valueOneDataType;

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

	@Column(name = "value_2_data_type", nullable=false)
	private String valueTwoDataType;

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
	
	@Column(name = "authoriser", length=100)
	private String authoriser;

	@OneToMany(mappedBy = "rule",fetch = FetchType.LAZY,
			cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@ToString.Exclude
	private Set<ServiceRule> serviceRule;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		Rule rule = (Rule) o;

		return Objects.equals(id, rule.id);
	}

	@Override
	public int hashCode() {
		return 667350611;
	}
}
