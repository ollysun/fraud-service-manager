package com.etz.fraudeagleeyemanager.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "product_service", uniqueConstraints = @UniqueConstraint(name="UC_PRODUCT_SERVICE",
        columnNames = {"product_code","service_name" }))
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

    @Column(name = "product_code", nullable = false, columnDefinition="VARCHAR(100)")
    private String productCode;

    @Column(name = "service_name", nullable = false, columnDefinition="VARCHAR(200)")
    private String serviceName;

    private String description;

    @Column(name = "callback_url", columnDefinition="VARCHAR(200)")
    private String callbackUrl;

    @Column(nullable = false, name = "status", columnDefinition = "TINYINT", length = 1)
    private Boolean status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_PRODUCT_CODE"), name = "product_code")
    @JsonBackReference
    @ToString.Exclude
    private ProductEntity productEntity;

    @ToString.Exclude
    @OneToMany(mappedBy = "productServiceEntity", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<ProductDataSet> productDataset;

}
