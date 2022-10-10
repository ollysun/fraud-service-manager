package com.etz.fraudeagleeyemanager.dto.request;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Data
public class CardRequest {
	@NotBlank(message="Please enter the  Card Holder Name")
	private String holderName;

	@NotBlank(message="Please enter the Card Bin")
	@PositiveOrZero(message="Please enter number only")
	private String cardBin;

	@NotNull(message="Please enter the CardBrand")
	private String cardBrand;


	@NotNull(message="Please enter the Card Type")
	private String cardType;

	@NotNull(message="Please enter the product Card expiry")
	private String expiry;

	@NotNull(message="Please enter the product Card Holder Name")
	@PositiveOrZero(message="Please enter positive integer for the CVV")
	private Integer cvv;

	@NotNull(message="Please enter the Card Issuing Bank Code")
	private String bankCode;

	@NotNull(message="Please enter the Card Issuing Bank Name")
	private String bankName;

	private String isoCountry;
	private String isoCountryCode;
	private Integer suspicionCount;
	private String blockReason;
	@JsonIgnore
	private String createdBy;
}
