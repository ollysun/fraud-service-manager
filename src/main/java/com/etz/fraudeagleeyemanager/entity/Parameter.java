package com.etz.fraudeagleeyemanager.entity;

import com.etz.fraudeagleeyemanager.constant.Status;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "parameter", 
uniqueConstraints = @UniqueConstraint(
		columnNames = {"name", "operator"}))
@Data
public class Parameter extends BaseEntity {
		
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@NotBlank(message = "Account number cannot be empty")
	@Column(name = "name")
	private String name;

	@NotBlank(message = "Account number cannot be empty")
	@Column(name = "operator")
	private String operator;

	@Column(name = "require_value")
	@Enumerated(EnumType.ORDINAL)
	private Status requireValue;
	
	@Column(name = "authorised")
	private Boolean authorised;
		
}
