package com.etz.fraudeagleeyemanager.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AccountProductId implements Serializable {
	private static final long serialVersionUID = 1L;

	private String productCode;

    private Long accountId;
}
