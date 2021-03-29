package com.etz.fraudeagleeyemanager.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class CardRequest {
	
	@JsonAlias("cardID")
	private Integer cardId;
	private String holderName;
	private Integer cardBin;
	private String cardBrand;
	private String cardType;
	private String expiry;
	private Integer cvv;
	private String bankCode;
	private String bankName;
	private String country;
	private String countryCode;
	private Integer suspicion;
	private Boolean status;
	
}
