package com.etz.fraudeagleeyemanager.entity;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.Transient;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

@MappedSuperclass
@Data
@EqualsAndHashCode(callSuper=true)
public abstract class BaseAuditVersionEntity<ID> extends BaseAuditEntity implements Serializable, Persistable<ID> {

	private static final long serialVersionUID = 1L;
	
	/*
	 * This resolves JPA calling merge instead of persist on a new entity that manually sets
	 * its primary key column.
	 */
	@JsonIgnore
	@Transient
	private boolean isNew = true; 
	
	@Override
	public boolean isNew() {
		return isNew;
	}

	@PrePersist
	@PostLoad
	void markNotNew() {
		this.isNew = false;
	}
	
}
