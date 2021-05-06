package com.etz.fraudeagleeyemanager.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import lombok.Data;

@Entity
@Table(name = "event_log")
@Data
public class EventLogEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "entity")
	private String entity;
	
	@Column(name = "entity_id")
	private String entityId;
	
	@Column(name = "event_desc")
	private String eventDesc;

	@Column(name = "record_before")
	@Type(type = "text")
	private String recordBefore;
	
	@Column(name = "record_after")
	@Type(type = "text")
	private String recordAfter;
	
	@Column(name = "endpoint")
	private String endpoint;
	
	@Column(name = "request_dump")
	@Type(type = "text")
	private String requestDump;
	
	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Column(name = "event_type", nullable = false)
	private String eventType;

	@Column(name = "event_time", nullable = false)
	private LocalDateTime eventTime;


}
