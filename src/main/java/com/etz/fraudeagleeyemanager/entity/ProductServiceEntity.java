package com.etz.fraudeagleeyemanager.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "product_service", uniqueConstraints = @UniqueConstraint(name="UC_PRODUCT_SERVICE",
        columnNames = {"product_code","service_name", "service_code" }),
        indexes = {@Index(name = "serviceNameUniqueIndex", columnList = "service_id, service_name")})
@SQLDelete(sql = "UPDATE product_service SET deleted = true, status=0 WHERE service_id = ?", check = ResultCheckStyle.COUNT)
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProductServiceEntity extends BaseAuditEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_id", nullable = false)
    private Long serviceId;

    @Column(name = "service_name", nullable = false, columnDefinition="VARCHAR(200)")
    private String serviceName;

    @Column(name = "service_code", nullable = false, columnDefinition="VARCHAR(200)")
    private String serviceCode;

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

}
