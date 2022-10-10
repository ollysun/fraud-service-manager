package com.etz.fraudeagleeyemanager.dto.request;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.etz.fraudeagleeyemanager.enums.UserCategory;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UpdateOfacWatchlistRequest implements Serializable{
	private static final long serialVersionUID = 1L;

	@NotNull(message="Please enter the Ofac Id")
	private Long ofacId;
	
	@NotBlank(message="Please enter the full name")
	private String fullName;

	@NotBlank(message="Please enter the Category")
	private String category;

	@NotBlank(message="Please enter the Comments")
	private String comments;
	
	@NotNull(message = "Please enter a status")
	private Boolean status;
	
	@NotBlank(message = "Please enter your username")
	private String updatedBy;
}
