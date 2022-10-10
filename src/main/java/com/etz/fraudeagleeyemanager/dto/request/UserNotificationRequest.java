package com.etz.fraudeagleeyemanager.dto.request;

import lombok.Data;

@Data
public class UserNotificationRequest {

	private String entity;
	private String entityId;
	private Integer notifType;
	private Long roleId;
	private Long userId;
	private String message;
	private String createdBy;

}
