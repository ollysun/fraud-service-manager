package com.etz.fraudeagleeyemanager.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;


@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "product_dataset")
@IdClass(ProductDatasetId.class)
public class ProductDataset extends BaseEntity implements Serializable {


	@Id
	@Column(name = "product_code", unique = true, nullable = false, columnDefinition="VARCHAR(100)")
	private String productCode;

	@Id
	@Column(name = "field_name", unique = true, nullable = false, columnDefinition="VARCHAR(100)")
	private String fieldName;

	@NotBlank(message = "data type cannot be empty")
	@Column(name = "data_type")
	private String dataType;

	@Column(nullable = false, name = "mandatory", columnDefinition = "TINYINT", length = 1)
	private Boolean mandatory;
	
	@Column(nullable = false, name = "authorised", columnDefinition = "TINYINT", length = 1)
	private Boolean authorised;

	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@MapsId("productCode")
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_PRODUCT_CODE"),
			name = "product_code",
			referencedColumnName="code")
	private Product product;

}
