package com.etz.fraudeagleeyemanager.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


@Entity
@Table(name = "product_dataset")
@Where(clause = "deleted = false")
@Getter
@Setter
@RequiredArgsConstructor
@ToString(exclude = { "productEntity" })
@IdClass(ProductDatasetId.class)
public class ProductDataSet extends BaseAuditVersionEntity<ProductDatasetId> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Id
	@Column(name = "product_code", nullable = false, columnDefinition="VARCHAR(100)")
	private String productCode;

	@Id
	@Column(name = "service_name",  nullable = false, columnDefinition="VARCHAR(250)")
	private String serviceName;

	@Column(name = "field_name",  nullable = false, columnDefinition="VARCHAR(250)")
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
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_PRODUCT_CODE"), name = "product_code")
	private ProductEntity productEntity;

	@JsonBackReference
	@ManyToOne
	@MapsId("serviceName")
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_SERVICE_NAME"), name = "service_name")
	private ProductServiceEntity productServiceEntity;

//	@Override
//	public ProductDatasetId getId() {
//		return new ProductDatasetId(id, productCode, fieldName);
//	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		ProductDataSet that = (ProductDataSet) o;

		if (!Objects.equals(id, that.id)) return false;
		if (!Objects.equals(productCode, that.productCode)) return false;
		return Objects.equals(serviceName, that.serviceName);
	}

	@Override
	public int hashCode() {
		int result = Objects.hashCode(id);
		result = 31 * result + (Objects.hashCode(productCode));
		result = 31 * result + (Objects.hashCode(serviceName));
		return result;
	}
}
