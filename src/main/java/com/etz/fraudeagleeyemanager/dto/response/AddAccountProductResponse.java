package com.etz.fraudeagleeyemanager.dto.response;

import java.io.Serializable;

import com.etz.fraudeagleeyemanager.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonIgnoreProperties({ "account", "productEntity" })
public class AddAccountProductResponse extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String productCode;
    private Long accountId;
    private Boolean status;

}
