package com.etz.fraudeagleeyemanager.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.*;

import lombok.*;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;



@Getter
@Setter
@ToString
@AllArgsConstructor
@Entity
@Table(name="email_group")
@SQLDelete(sql = "UPDATE email_group SET deleted = true, status=0 WHERE id = ?", check = ResultCheckStyle.COUNT)
@NoArgsConstructor
public class EmailGroup extends BaseAuditEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "group_name", unique = true)
	private String groupName;

	@Column(name = "email", columnDefinition = "TEXT")
	private String email;
	
	@Column(nullable = false, name = "status", columnDefinition = "TINYINT", length = 1)
	private Boolean status;

	@ToString.Exclude
	@OneToOne(mappedBy = "emailGroup", fetch = FetchType.LAZY,
			cascade = CascadeType.ALL)
	private ServiceRule serviceRule;

	@OneToMany(mappedBy = "emailGroup", fetch = FetchType.LAZY,
			cascade = {CascadeType.MERGE,CascadeType.PERSIST}, orphanRemoval = true)
	@ToString.Exclude
	private Set<ReportScheduler> reportSchedulers;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		EmailGroup that = (EmailGroup) o;

		return id != null && id.equals(that.id);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
