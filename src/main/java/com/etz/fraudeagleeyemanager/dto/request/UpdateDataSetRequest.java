package com.etz.fraudeagleeyemanager.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class UpdateDataSetRequest implements Serializable {
	private static final long serialVersionUID = 1L;

    @NotBlank(message="Please enter the product code")
    private String productCode;

	@NotBlank(message="Please enter the product serviceId")
    private String serviceId;

    @NotNull(message="Please enter the datasetId")
    private Long datasetId;

    @NotBlank(message="Please enter the data Type")
    private String dataType;

    @NotBlank(message="Please enter the fieldName")
    private String fieldName;

    @NotNull(message="Please tell the compulsory status")
    private Boolean compulsory;

    @JsonIgnore
    private String updatedBy;

    @NotNull(message="Please state the status of the authorization")
    private Boolean authorised;
}
