package com.etz.fraudeagleeyemanager.dto.response;

import com.etz.fraudeagleeyemanager.entity.ProductEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

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
