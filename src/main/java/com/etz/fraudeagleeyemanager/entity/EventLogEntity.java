package com.etz.fraudeagleeyemanager.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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

	@Column(name = "event_time", nullable = false)
	private LocalDateTime eventTime;
	
	@Column(name = "event_type", nullable = false)
	private String eventType;
	
	@Column(name = "event_desc")
	private String eventDesc;

	@Column(name = "user_id", nullable = false)
	private Long userId;

}
