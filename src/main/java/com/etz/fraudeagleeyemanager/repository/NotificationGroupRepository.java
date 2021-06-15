package com.etz.fraudeagleeyemanager.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.etz.fraudeagleeyemanager.entity.NotificationGroup;

public interface NotificationGroupRepository extends JpaRepository<NotificationGroup, Long> {

	Optional<NotificationGroup> findByGroupName(String groupName);
	
	Optional<NotificationGroup> findByIdAndGroupName(Long id, String groupName);
}
