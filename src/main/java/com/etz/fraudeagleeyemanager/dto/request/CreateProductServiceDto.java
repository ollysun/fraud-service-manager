package com.etz.fraudeagleeyemanager.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductServiceDto  {
    @NotBlank(message = "Please enter the serviceId")
    private String serviceId;
    @NotBlank(message = "Please enter the product code")
    private String productCode;
    @NotBlank(message = "please enter the service name")
    private String serviceName;
    private String description;
    private String callback;
    @JsonIgnore
    private String createdBy;
}
