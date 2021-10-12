package com.etz.fraudeagleeyemanager.dto.request;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.Data;

@Data
public class UpdateUserNotificationRequest {
	@NotNull
	@JsonAlias("notificationId")
	private Long id;
	private Long userId;
	private String updatedBy;
}
