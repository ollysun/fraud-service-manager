package com.etz.fraudeagleeyemanager.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.io.Serializable;

@MappedSuperclass
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class BaseAuditEntity extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
    
	@JsonIgnore
    @Transient
    private String entity = ""; //Affected Table Name
    
	@JsonIgnore
    @Transient
    private String entityId = ""; //Affected Table Id
    
	@JsonIgnore
    @Transient
    private String eventDescription = ""; //Activity Description
    
	@JsonIgnore
	@Transient
    private String recordBefore = ""; //Records before action was performed
	
	@JsonIgnore
	@Transient
	private String recordAfter = ""; //Records after action
	
	@JsonIgnore
	@Transient
	private String endpoint = ""; //Endpoint called for performed action
	
	@JsonIgnore
	@Transient
	private Object requestDump = null; //Stringify dump of json request
	
	@JsonIgnore
    @Transient
    private Long userId;

	@JsonIgnore
    @Transient
    private String eventType = ""; //Event activity type i.e INSERT, UPDATE etc

}
