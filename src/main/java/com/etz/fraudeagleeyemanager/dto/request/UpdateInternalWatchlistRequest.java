package com.etz.fraudeagleeyemanager.dto.request;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class UpdateInternalWatchlistRequest implements Serializable{
	private static final long serialVersionUID = 1L;

	@NotNull(message = "Please enter the watchId")
	private Long watchId;
	
	@NotBlank(message="Please enter BVN")
	@PositiveOrZero(message = "Please enter a correct BVN number")
	@Size(max = 11, min = 11, message="Please enter the correct BVN number")
	private String bvn;

	@NotBlank(message="Please enter the Comments")
	private String comments;
	
	@NotNull(message = "Please enter a Status")
	private Boolean status;
	
	@NotBlank(message = "Please enter your Username")
	private String updatedBy;
}
