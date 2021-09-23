package com.etz.fraudeagleeyemanager.dto.response;

import com.etz.fraudeagleeyemanager.entity.eagleeyedb.Account;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@JsonIgnoreProperties({ "accountProducts"})
public class AccountResponse extends Account {
	private static final long serialVersionUID = 1L;
}
