package com.etz.fraudeagleeyemanager.dto.request;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.etz.fraudeagleeyemanager.enums.UserCategory;

import lombok.Data;

@Data
public class OfacWatchlistRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	@NotBlank(message="Please enter the full name")
	private String fullName;

	@NotNull(message="Please enter the Category")
	private UserCategory category;

	@NotBlank(message="Please enter the Comments")
	private String comments;
	
	@NotBlank(message = "Please enter your username")
	private String createdBy;
}
