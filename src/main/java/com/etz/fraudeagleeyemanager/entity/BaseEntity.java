package com.etz.fraudeagleeyemanager.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@MappedSuperclass
public class BaseEntity {

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @NotBlank(message = "Please enter the name of the creator")
    @Column(name = "created_by", nullable = false)
    private String createdBy;
 
    @Column(name = "updated_at")
    @UpdateTimestamp
    @LastModifiedDate
    private LocalDateTime updatedAt;
 
    @Column(name = "updated_by")
    private String updatedBy;
 
}