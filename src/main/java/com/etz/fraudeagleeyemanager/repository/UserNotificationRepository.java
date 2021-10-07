package com.etz.fraudeagleeyemanager.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.etz.fraudeagleeyemanager.entity.UserNotification;

@Repository
public interface UserNotificationRepository extends JpaRepository<UserNotification, Long> {

	Optional<List<UserNotification>> findByRoleId(Long roleId);
	
	Optional<List<UserNotification>> findByUserID(Long userID);
	
	Optional<List<UserNotification>> findByRoleIdAndUserID(Long roleId, Long userID);
	
	Optional<List<UserNotification>> findByEntityNameAndEntityID(String entityName, String entityID);
}
