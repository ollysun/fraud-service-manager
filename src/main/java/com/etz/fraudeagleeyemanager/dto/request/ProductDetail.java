package com.etz.fraudeagleeyemanager.dto.request;

import com.etz.fraudeagleeyemanager.constant.BooleanStatus;
import com.etz.fraudeagleeyemanager.constant.Status;
import lombok.Data;

@Data
public class ProductDetail {

	private String productCode;
	private String productName;
	private String productDesc;
	private BooleanStatus useCard;
	private BooleanStatus useAccount;
	private String callback;
	private Status status;
	
}
