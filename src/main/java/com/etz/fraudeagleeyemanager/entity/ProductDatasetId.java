package com.etz.fraudeagleeyemanager.entity;

import lombok.*;
import java.io.*;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ProductDatasetId implements Serializable{

    private String productCode;

    private String fieldName;
}
