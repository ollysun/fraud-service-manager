package com.etz.fraudeagleeyemanager.dto.request;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class MapRuleToServiceRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	@NotBlank(message="serviceId cannot be empty")
	private String serviceId;
	@NotNull(message="ruleId cannot be empty")
	private List<Long> ruleId;
	private Boolean notifyAdmin;
	private Long notificationGroupId;
	private Boolean notifyCustomer;
	@NotNull(message="authorised cannot be empty")
	private Boolean authorised;
	@JsonIgnore
	private String createdBy;

}
