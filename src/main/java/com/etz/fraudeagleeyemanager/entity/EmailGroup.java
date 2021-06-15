package com.etz.fraudeagleeyemanager.entity;

import com.etz.fraudeagleeyemanager.constant.Status;
import lombok.*;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name="email_group")
@SQLDelete(sql = "UPDATE email_group SET deleted = true WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted = false")
public class EmailGroup extends BaseAuditEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "group_name", unique = true)
	private String groupName;

	@Column(name = "email", columnDefinition = "TEXT")
	private String email;
	
	@Column(nullable = false, name = "status", columnDefinition = "TINYINT", length = 1)
	@Enumerated(EnumType.ORDINAL)
	private Status status;

	@ToString.Exclude
	@OneToOne(mappedBy = "emailGroup", fetch = FetchType.LAZY,
			cascade = CascadeType.ALL)
	private ProductRule productRule;

//	@ToString.Exclude
//	@OneToMany(mappedBy = "emailGroup", fetch = FetchType.EAGER,
//			cascade = CascadeType.ALL, orphanRemoval = true)
//	private Set<ReportScheduler> reportSchedulers = new HashSet<>();

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
