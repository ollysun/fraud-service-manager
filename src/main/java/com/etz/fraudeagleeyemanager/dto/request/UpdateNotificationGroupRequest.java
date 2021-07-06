package com.etz.fraudeagleeyemanager.dto.request;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class UpdateNotificationGroupRequest implements Serializable{
	private static final long serialVersionUID = 1L;

	private Long groupId;
	
	@NotBlank(message = "Please enter the group name")
	private String name;
	
	@NotNull(message = "Please list of email addresses")
	private List<String> emails;
	
	@NotNull(message = "Please list of phone numbers")
	private List<String> phoneNos;
	
	@NotNull(message = "Please indicate true/false for mailAlert")
	private Boolean mailAlert;
	
	@NotNull(message = "Please indicate true/false for smsAlert")
	private Boolean smsAlert;
	
	@NotNull(message = "Please enter a status")
	private Boolean status;
	
	@NotBlank(message = "Please enter your username")
	private String updatedBy;
}
