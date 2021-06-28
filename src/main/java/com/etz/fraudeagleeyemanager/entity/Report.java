package com.etz.fraudeagleeyemanager.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "report")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Report extends BaseAuditEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Name cannot be empty")
	@Column(name = "name")
	private String name;

	@NotBlank(message = "Description cannot be empty")
	@Column(name = "description")
	private String description;

	@ToString.Exclude
	@OneToMany(mappedBy = "report", fetch = FetchType.EAGER,
			cascade = CascadeType.ALL)
	private Set<ReportScheduler> reportSchedulers = new HashSet<>();

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Report that = (Report) o;

		return id != null && id.equals(that.id);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
