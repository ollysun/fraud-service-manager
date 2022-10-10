package com.etz.fraudeagleeyemanager.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "product_service", uniqueConstraints = @UniqueConstraint(name="UC_PRODUCT_SERVICE",
        columnNames = {"product_code","service_name", "service_id" }),
        indexes = {@Index(name = "serviceNameUniqueIndex", columnList = "service_id, service_name, product_code")})
@SQLDelete(sql = "UPDATE product_service SET deleted = true, status=0 WHERE service_id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted=false")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProductServiceEntity  extends BaseAuditVersionEntity<String> implements Serializable {
	private static final long serialVersionUID = 1L;
	
    @Id
    @Column(name = "service_id", nullable = false)
    private String serviceId;

    @Column(name = "service_name", nullable = false, columnDefinition="VARCHAR(200)")
    private String serviceName;

    @Column(name = "product_code", nullable = false, columnDefinition="VARCHAR(100)")
    private String productCode;

    private String description;

    @Column(name = "callback_url", columnDefinition="VARCHAR(200)")
    private String callbackUrl;

    @Column(nullable = false, name = "status", columnDefinition = "TINYINT", length = 1)
    private Boolean status;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productCode")
    @JoinColumn(name = "product_code",foreignKey = @ForeignKey(name = "FK_PRODUCT_CODE"))
    @JsonBackReference
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private ProductEntity productEntity;

    @ToString.Exclude
    @OneToMany(mappedBy = "productServiceEntity", fetch = FetchType.LAZY,
            cascade = {CascadeType.MERGE,CascadeType.PERSIST}, orphanRemoval = true)
    @JsonManagedReference
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Set<ServiceDataSet> serviceDataset;

	@Override
	public String getId() {
		return serviceId;
	}
}
