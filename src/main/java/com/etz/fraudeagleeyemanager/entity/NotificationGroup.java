package com.etz.fraudeagleeyemanager.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name = "notification_groups")
@SQLDelete(sql = "UPDATE account SET deleted = true WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted = false")
@ToString
@Data
@EqualsAndHashCode(callSuper = true)
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
	
	@Column(name = "authoriser", nullable = true, length=100) //at creation authoriser will not be provided
	private String authoriser;
	
	@Column(name = "status", nullable = false, columnDefinition = "TINYINT", length = 1)
	private Boolean status;
	
}
