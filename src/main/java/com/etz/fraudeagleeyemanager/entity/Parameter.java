package com.etz.fraudeagleeyemanager.entity;

import com.etz.fraudeagleeyemanager.constant.Status;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "parameter", 
uniqueConstraints = @UniqueConstraint(
		columnNames = {"name", "operator"}, name = "UC_Parameter"))
@SQLDelete(sql = "UPDATE parameter SET deleted = true WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted = false")
@Data
public class Parameter extends BaseEntity {
		
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name", columnDefinition="VARCHAR(150)")
	private String name;

	@Column(name = "operator",  columnDefinition="VARCHAR(100)")
	private String operator;

	@Column(name = "require_value")
	private Boolean requireValue;
	
	@Column(name = "authorised")
	private Boolean authorised;
		
}
