package com.etz.fraudeagleeyemanager.entity;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "parameter", 
uniqueConstraints = @UniqueConstraint(
		columnNames = {"name", "operator"}, name = "UC_Parameter"))
@SQLDelete(sql = "UPDATE parameter SET deleted = true WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted=false")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Parameter extends BaseAuditEntity implements Serializable {
	private static final long serialVersionUID = 1L;

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
	
	@Column(name = "authoriser", length=100)
	private String authoriser;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		Parameter parameter = (Parameter) o;

		return Objects.equals(id, parameter.id);
	}

	@Override
	public int hashCode() {
		return 1131502493;
	}
}
