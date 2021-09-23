package com.etz.fraudeagleeyemanager.dto.response;

import java.io.Serializable;

import com.etz.fraudeagleeyemanager.entity.eagleeyedb.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonIgnoreProperties({ "createdAt", "createdBy" })
public class AccountProductResponse extends BaseEntity implements Serializable {

	private String productCode;

    private Long accountId;

    private Boolean status;


}
