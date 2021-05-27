package com.etz.fraudeagleeyemanager.entity;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

@MappedSuperclass
@Data
@EqualsAndHashCode(callSuper=true)
public class BaseAuditVersionEntity extends BaseAuditEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/*
	 * This resolves JPA calling merge instead of persist on a new entity that manually sets
	 * its primary key column.
	 */
	@JsonIgnore
    @Version
    private Long version;
	
}
