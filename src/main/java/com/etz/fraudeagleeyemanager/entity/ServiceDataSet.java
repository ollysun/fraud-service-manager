package com.etz.fraudeagleeyemanager.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.Hibernate;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


@Entity
@Table(name = "service_dataset",
		indexes = {@Index(name = "serviceNameUniqueIndex", columnList = "service_id, id, product_code")})
@Getter
@Setter
@AllArgsConstructor
@ToString(exclude = { "productEntity" })
@IdClass(ProductDatasetId.class)
@NoArgsConstructor
public class ServiceDataSet extends BaseAuditVersionEntity<ProductDatasetId> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Id
	@Column(name = "product_code", nullable = false, columnDefinition="VARCHAR(100)")
	private String productCode;

	@Id
	@Column(name = "service_id",  nullable = false)
	private String serviceId;

	@Column(name = "field_name",  nullable = false, columnDefinition="VARCHAR(250)")
	private String fieldName;

	@Column(name = "data_type")
	private String dataType;

	@Column(nullable = false, name = "mandatory", columnDefinition = "TINYINT", length = 1)
	private Boolean mandatory;
	
	@Column(nullable = false, name = "authorised", columnDefinition = "TINYINT", length = 1)
	private Boolean authorised;

	@ManyToOne
	@MapsId("productCode")
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_PRODUCT_CODE"), name = "product_code")
	@JsonBackReference
	private ProductEntity productEntity;

	@ManyToOne
	@MapsId("serviceId")
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_SERVICE_ID"), name = "service_id")
	@JsonBackReference
	private ProductServiceEntity productServiceEntity;

	@Override
	public ProductDatasetId getId() {
		return new ProductDatasetId(id, productCode, serviceId);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		ServiceDataSet that = (ServiceDataSet) o;

		if (!Objects.equals(id, that.id)) return false;
		if (!Objects.equals(productCode, that.productCode)) return false;
		return Objects.equals(serviceId, that.serviceId);
	}

	@Override
	public int hashCode() {
		int result = Objects.hashCode(id);
		result = 31 * result + (Objects.hashCode(productCode));
		result = 31 * result + (Objects.hashCode(serviceId));
		return result;
	}
}
