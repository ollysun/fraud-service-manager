package com.etz.fraudeagleeyemanager.dto.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class UpdateDataSetRequest implements Serializable {

    @NotNull(message="Please enter the product Code")
    private String productCode;

    @NotNull(message="Please enter the field name")
    private String fieldName;

    @NotNull(message="Please enter the data Type")
    private String dataType;

    @NotNull(message="Please tell the compulsory status")
    private Boolean compulsory;

    @NotNull(message="Please enter the updated by name")
    private String updatedBy;

    @NotNull(message="Please state the status of the authorization")
    private Boolean authorised;
}
