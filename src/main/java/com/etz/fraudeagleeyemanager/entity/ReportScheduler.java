package com.etz.fraudeagleeyemanager.entity;

import com.etz.fraudeagleeyemanager.enums.ExportType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import javax.persistence.*;

import com.etz.fraudeagleeyemanager.enums.IntervalType;

import java.io.Serializable;

@Entity
@Table(name = "report_scheduler",
		uniqueConstraints = @UniqueConstraint(
				columnNames = {"report_id", "notification_group"}, name = "UC_REPORT_EMAIL"))
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
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

	@Column(name = "export_type", nullable = false,  columnDefinition = "VARCHAR(45)", length = 20)
	@Enumerated(EnumType.STRING)
	private ExportType exportType;
	
	@Column(nullable = false, name = "report_loop", columnDefinition = "TINYINT", length = 1)
	private Boolean reportLoop;

	@Column(nullable = false, name = "status", columnDefinition = "TINYINT", length = 1)
	private Boolean status;

	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "report_id", referencedColumnName="id", nullable = false)
	private Report report;

	@ToString.Exclude
	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "notification_group", foreignKey = @ForeignKey(name = "FK_REPORT_SCHEDULER_NOTIFICATION_GROUP_ID"))
	private NotificationGroup notificationGroup;


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
