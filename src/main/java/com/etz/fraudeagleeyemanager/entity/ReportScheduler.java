package com.etz.fraudeagleeyemanager.entity;

import com.etz.fraudeagleeyemanager.constant.IntervalType;
import com.etz.fraudeagleeyemanager.constant.Status;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "report_scheduler",
		uniqueConstraints = @UniqueConstraint(
				columnNames = {"report_id", "email_group"}, name = "UC_REPORT_EMAIL"))
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class ReportScheduler extends BaseAuditEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "interval_value")
	private Integer intervalValue;
	
	@Column(name = "interval_type", nullable = false,  columnDefinition = "VARCHAR(45)", length = 20)
	@Enumerated(EnumType.STRING)
	private IntervalType intervalType;
	
	@Column(nullable = false, name = "\"loop\"", columnDefinition = "TINYINT", length = 1)
	private Boolean loop;

	@Column(nullable = false, name = "status", columnDefinition = "TINYINT", length = 1)
	@Enumerated(EnumType.ORDINAL)
	private Status status;

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
