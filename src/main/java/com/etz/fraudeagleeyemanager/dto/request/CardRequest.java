package com.etz.fraudeagleeyemanager.dto.request;

import com.etz.fraudeagleeyemanager.constant.CardBrand;
import com.etz.fraudeagleeyemanager.constant.CardType;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serializable;

@Data
public class CardRequest implements Serializable {
	@NotNull(message="Please enter the  Card Holder Name")
	private String holderName;

	@NotNull(message="Please enter the Card Bin")
	@PositiveOrZero
	private Integer cardBin;

	@NotNull(message="Please enter the CardBrand")
	private CardBrand cardBrand;

	@NotNull(message="Please enter the Card Type")
	private CardType cardType;

	@NotNull(message="Please enter the product Card expiry")
	private String expiry;

	@NotNull(message="Please enter the product Card Holder Name")
	@PositiveOrZero(message="Please enter positive integer for the CVV")
	private Integer cvv;

	@NotNull(message="Please enter the Card Issuing Bank Code")
	private String bankCode;

	@NotNull(message="Please enter the Card Issuing Bank Name")
	private String bankName;

	private String country;
	private String countryCode;
	private Integer suspicionCount;

	private String blockReason;
	@NotNull(message="Please give the status of the Card")
	private Boolean status;
	
}
