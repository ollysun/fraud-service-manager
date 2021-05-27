package com.etz.fraudeagleeyemanager.entity;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ProductDatasetId implements Serializable{

    private Long id;

    @NotNull
    private String productCode;

    @NotNull
    private String fieldName;
}
