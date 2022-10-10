package com.etz.fraudeagleeyemanager.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
public class CreateReportRequest {
    @NotBlank(message="Please enter the report name")
    private String name;
    @NotBlank(message="Please enter the report description")
    private String description;
    @NotBlank(message="Please enter the report createdBy")
    private String createdBy;
}
