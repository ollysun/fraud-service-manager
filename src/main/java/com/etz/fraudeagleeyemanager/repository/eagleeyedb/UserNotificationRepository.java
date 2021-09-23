package com.etz.fraudeagleeyemanager.repository.eagleeyedb;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.etz.fraudeagleeyemanager.entity.eagleeyedb.UserNotification;

@Repository
public interface UserNotificationRepository extends JpaRepository<UserNotification, Long> {

	Optional<List<UserNotification>> findByRoleId(Long roleId);
	
	Optional<List<UserNotification>> findByUserId(Long userId);
	
	Optional<List<UserNotification>> findByRoleIdAndUserId(Long roleId, Long userId);
}
