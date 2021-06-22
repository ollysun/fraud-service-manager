package com.etz.fraudeagleeyemanager.entity;

import lombok.*;
import javax.persistence.*;

import com.etz.fraudeagleeyemanager.enums.IntervalType;

import java.io.Serializable;

@Entity
@Table(name = "report_scheduler",
		uniqueConstraints = @UniqueConstraint(
				columnNames = {"report_id", "email_group"}, name = "UC_REPORT_EMAIL"))
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class ReportScheduler extends BaseAuditEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "interval_value")
	private Integer intervalValue;
	
	@Column(name = "interval_type", nullable = false,  columnDefinition = "VARCHAR(45)", length = 20)
	@Enumerated(EnumType.STRING)
	private IntervalType intervalType;
	
	@Column(nullable = false, name = "report_loop", columnDefinition = "TINYINT", length = 1)
	private Boolean reportLoop;

	@Column(nullable = false, name = "status", columnDefinition = "TINYINT", length = 1)
	private Boolean status;

	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "report_id", referencedColumnName="id", nullable = false)
	private Report report;

	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "email_group", referencedColumnName="id", nullable = false)
	private EmailGroup emailGroup;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ReportScheduler that = (ReportScheduler) o;

		return id != null && id.equals(that.id);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}


}
