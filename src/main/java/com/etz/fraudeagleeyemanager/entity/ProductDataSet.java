package com.etz.fraudeagleeyemanager.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.Where;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


@Entity
@Table(name = "product_dataset")
@Where(clause = "deleted = false")
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(exclude = { "productEntity" })
@IdClass(ProductDatasetId.class)
public class ProductDataSet extends BaseAuditEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Id
	@Column(name = "product_code",  nullable = false)
	private String productCode;

	@Id
	@Column(name = "field_name",  nullable = false)
	private String fieldName;

	@NotBlank(message = "data type cannot be empty")
	@Column(name = "data_type")
	private String dataType;

	@Column(nullable = false, name = "mandatory", columnDefinition = "TINYINT", length = 1)
	private Boolean mandatory;
	
	@Column(nullable = false, name = "authorised", columnDefinition = "TINYINT", length = 1)
	private Boolean authorised;

	@ManyToOne
	@MapsId("productCode")
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_PRODUCT_CODE"),
			name = "product_code",
			referencedColumnName="code")
	private ProductEntity productEntity;

}
