package com.etz.fraudeagleeyemanager.entity.eagleeyedb;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.Hibernate;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "user_notification")
@SQLDelete(sql = "UPDATE user_notification SET deleted = true, status=0 WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted=false")
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(value = {"roleId", "userId", "system", "status", "version", "delete"})
public class UserNotification extends BaseAuditEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "entity", nullable = false)
	private String entityName;
	
	@Column(name = "entity_id", nullable = false)
	private String entityID;
	
	@Column(name = "notif_type", nullable = false)
	private int notifyType;

	@Column(name = "role_id")
	private Long roleId;
	
	@Column(name = "user_id")
	private Long userId;
	
	@Column(name = "system", columnDefinition = "TINYINT", length = 1)
	private Boolean system = false;
	
	@Column(name = "message")
	@Type(type = "text")
	private String message;
	
	@Column(name = "status", nullable = false, columnDefinition = "TINYINT", length = 1)
	private Boolean status;
	
	@Column(name = "version")
	@Version
	private int version;


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		UserNotification that = (UserNotification) o;

		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return 1854275613;
	}
}
