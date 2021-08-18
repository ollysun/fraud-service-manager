package com.etz.fraudeagleeyemanager.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CreateReportSchedulerRequest {
    private Long reportId;
    private Long notificationId;
    private Integer integer;
    private String intervalType;
    private Boolean loop;
    private String exportType;
    @JsonIgnore
    private String createdBy;
}
