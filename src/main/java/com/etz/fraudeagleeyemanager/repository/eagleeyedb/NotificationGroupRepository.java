package com.etz.fraudeagleeyemanager.repository.eagleeyedb;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.etz.fraudeagleeyemanager.entity.eagleeyedb.NotificationGroup;

public interface NotificationGroupRepository extends JpaRepository<NotificationGroup, Long>, JpaSpecificationExecutor<NotificationGroup> {

	Optional<NotificationGroup> findByGroupName(String groupName);
	
	Optional<NotificationGroup> findByIdAndGroupName(Long id, String groupName);
}
