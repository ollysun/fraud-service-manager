package com.etz.fraudeagleeyemanager.dto.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class UpdateCardRequestDto implements Serializable {
    private Integer cardBin;
    private Integer count;
    private Boolean status;
    private String blockReason;
}
