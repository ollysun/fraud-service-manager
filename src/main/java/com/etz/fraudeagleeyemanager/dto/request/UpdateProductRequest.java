package com.etz.fraudeagleeyemanager.dto.request;

import com.etz.fraudeagleeyemanager.constant.Status;
import lombok.Data;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class UpdateProductRequest implements Serializable {
	
	@NotNull(message="Please enter the product Code")
	private String productCode;

	@NotNull(message="Please enter the product Name")
	private String productName;

	private String productDesc;

	@NotNull(message = "Please let know if the user will use card")
	private Boolean useCard;

	@NotNull(message = "Please let know if the user will use Account")
	private Boolean useAccount;

	private String callback;

	@NotNull(message = "Please let know the status ")
	private Status status;

	@NotNull(message = "UpdatedBy cannot be empty")
	private String updatedBy;
}
