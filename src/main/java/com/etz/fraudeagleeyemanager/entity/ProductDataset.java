package com.etz.fraudeagleeyemanager.entity;

import com.etz.fraudeagleeyemanager.constant.BooleanStatus;
import com.etz.fraudeagleeyemanager.constant.Status;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;


@Entity
@Table(name = "product_dataset")
@Getter
@Setter
@RequiredArgsConstructor
@ToString
@IdClass(ProductDatasetKey.class)
public class ProductDataset extends BaseEntity implements Serializable {

	@Id
	@Column(name = "product_code")
	private String productCode;

	@Id
	@Column(name = "field_name")
	private String fieldName;

	@NotBlank(message = "data type cannot be empty")
	@Column(name = "data_type")
	private String dataType;

	@Column(nullable = false, name = "mandatory", columnDefinition = "TINYINT", length = 1)
	@Enumerated(EnumType.ORDINAL)
	private BooleanStatus mandatory;
	
	@Column(nullable = false, name = "authorised", columnDefinition = "TINYINT", length = 1)
	@Enumerated(EnumType.ORDINAL)
	private Status authorised;

	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@MapsId("productCode")
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_PRODUCT_CODE"),
			name = "product_code",
			referencedColumnName="code")
	private Product product;

}
