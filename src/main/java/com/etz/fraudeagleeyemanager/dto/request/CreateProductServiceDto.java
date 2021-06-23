package com.etz.fraudeagleeyemanager.dto.request;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductServiceDto {
    @NotBlank
    private String productCode;
    @NotBlank
    private String serviceName;
    private String description;
    private String callback;
    private String createdBy;
}
