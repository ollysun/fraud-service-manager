package com.etz.fraudeagleeyemanager.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "product_dataset")
@Where(clause = "deleted = false")
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(exclude = { "productEntity" })
@IdClass(ProductDatasetId.class)
public class ProductDataSet extends BaseEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Id
	@Column(name = "product_code",  nullable = false)
	private String productCode;

	@Id
	@Column(name = "field_name",  nullable = false)
	private String fieldName;

	@Column(name = "data_type")
	private String dataType;

	@Column(nullable = false, name = "mandatory", columnDefinition = "TINYINT", length = 1)
	private Boolean mandatory;
	
	@Column(nullable = false, name = "authorised", columnDefinition = "TINYINT", length = 1)
	private Boolean authorised;

	@JsonBackReference
	@ManyToOne
	@MapsId("productCode")
	//@JoinColumn(foreignKey = @ForeignKey(name = "FK_PRODUCT_CODE"), name = "product_code", referencedColumnName="code")
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_PRODUCT_CODE"), name = "product_code")
	private ProductEntity productEntity;

}
