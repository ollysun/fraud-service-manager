package com.etz.fraudeagleeyemanager.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UpdateReportSchedulerRequest extends CreateReportSchedulerRequest{
    private int reportSchedulerId;
    private Boolean status;
}
