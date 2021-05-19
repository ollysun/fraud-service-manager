package com.etz.fraudeagleeyemanager.entity;

import lombok.*;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;


@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "product_dataset")
@SQLDelete(sql = "UPDATE product_dataset SET deleted = true WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted = false")
@IdClass(ProductDatasetId.class)
public class ProductDataSet extends BaseEntity implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Id
	@Column(name = "product_code",  nullable = false, columnDefinition="VARCHAR(100)")
	private String productCode;

	@Id
	@Column(name = "field_name",  nullable = false, columnDefinition="VARCHAR(100)")
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
	private ProductEntity productEntity;

}
