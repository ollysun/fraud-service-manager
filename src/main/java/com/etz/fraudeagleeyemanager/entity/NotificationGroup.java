package com.etz.fraudeagleeyemanager.entity;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "notification_groups")
@SQLDelete(sql = "UPDATE account SET deleted = true, status=0 WHERE id = ?", check = ResultCheckStyle.COUNT)
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationGroup extends BaseAuditEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@Column(name = "group_name", unique = true)
	private String groupName;

	@Column(name = "emails")
	@Type(type = "text")
	private String emails;
	
	@Column(name = "phones")
	@Type(type = "text")
	private String phones;
	
	@Column(name = "email_alert", nullable = false, columnDefinition = "TINYINT", length = 1)
	private Boolean emailAlert;
	
	@Column(name = "sms_alert", nullable = false, columnDefinition = "TINYINT", length = 1)
	private Boolean smsAlert;
	
	@Column(name = "authorised", nullable = false, columnDefinition = "TINYINT", length = 1)
	private Boolean authorised;
	
	@Column(name = "authoriser", length=100) //at creation authoriser will not be provided
	private String authoriser;
	
	@Column(name = "status", nullable = false, columnDefinition = "TINYINT", length = 1)
	private Boolean status;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		NotificationGroup that = (NotificationGroup) o;

		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return 1854275612;
	}
}
