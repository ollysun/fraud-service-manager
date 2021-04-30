package com.etz.fraudeagleeyemanager.entity;

import lombok.*;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.*;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ProductDatasetId implements Serializable{


    private Long id;

    private String productCode;

    private String fieldName;
}
