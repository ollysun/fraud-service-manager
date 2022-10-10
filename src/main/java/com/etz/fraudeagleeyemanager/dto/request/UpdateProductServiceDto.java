package com.etz.fraudeagleeyemanager.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@ToString
public class UpdateProductServiceDto implements Serializable {
    @NotNull(message = "Please enter the service id")
    private String serviceId;
    @NotBlank(message = "Please enter the product code")
    private String productCode;
    @NotBlank(message = "Please enter the service name")
    private String serviceName;
    private String description;
    private String callback;

    @JsonIgnore
    private String updatedBy;
    @NotNull(message = "Please enter the status")
    private Boolean status;

}
