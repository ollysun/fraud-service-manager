package com.etz.fraudeagleeyemanager.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table
@Setter
@Getter
public class Student extends BaseEntity{
 
    @EmbeddedId
    private StudentId id;
 
    @Column
    private String name;
}