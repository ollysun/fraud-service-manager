package com.etz.fraudeagleeyemanager.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties({ "productDataset", "productRules", "products", "productLists" })
public class ProductResponse  {
    private String code;

    private String name;

    private String description;

    private Boolean useCard;

    private Boolean useAccount;

    private String callbackURL;

    private Boolean supportHold;

    private Boolean status;

    private LocalDateTime createdAt;

    private String createdBy;

    private LocalDateTime updatedAt;

    private String updatedBy;

    private boolean deleted;

}
