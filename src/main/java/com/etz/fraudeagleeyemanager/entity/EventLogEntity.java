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

	@Column(name = "entity", nullable = false, columnDefinition="VARCHAR(150)")
	private String entity;
	
	@Column(name = "entity_id", nullable = true, columnDefinition="VARCHAR(250)")
	private String entityId;
	
	@Column(name = "event_desc", columnDefinition="VARCHAR(250)")
	private String eventDesc;

	@Column(name = "record_before")
	@Type(type = "text")
	private String recordBefore;
	
	@Column(name = "record_after")
	@Type(type = "text")
	private String recordAfter;
	
	@Column(name = "endpoint", columnDefinition="VARCHAR(250)")
	private String endpoint;
	
	@Column(name = "request_dump")
	@Type(type = "text")
	private String requestDump;
	
	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Column(name = "event_type", nullable = false, columnDefinition="VARCHAR(100)")
	private String eventType;

	@Column(name = "event_time", nullable = false)
	private LocalDateTime eventTime;

}
