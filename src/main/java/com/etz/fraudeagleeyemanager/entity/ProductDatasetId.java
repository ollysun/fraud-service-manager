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
	private static final long serialVersionUID = 1L;

	private Long datasetId;

    @NotNull
    private String productCode;

    @NotNull
    private String serviceId;
}
