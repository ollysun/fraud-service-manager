package com.etz.fraudeagleeyemanager.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "product", indexes = {
        @Index(name = "uniqueProductIndex", columnList = "code, name", unique = true)
})
@SQLDelete(sql = "UPDATE product SET deleted = true, status=0 WHERE code = ?", check = ResultCheckStyle.COUNT)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductEntity extends BaseAuditVersionEntity<String> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
    @Column(name = "code", columnDefinition="VARCHAR(100)")
    private String code;

    @Column(nullable = false, name = "name", length = 200)
    private String name;

    @Column(name = "description")
    private String description;


    @Column(nullable = false, name = "use_card", columnDefinition = "TINYINT default true", length = 1)
    private Boolean useCard;

    @Column(nullable = false,name = "use_account", columnDefinition = "TINYINT", length = 1)
    private Boolean useAccount;

    @Column(name = "callback_url")
    private String callbackURL;

    @Column( name = "support_hold", columnDefinition = "TINYINT default 0", length = 1)
    private Boolean supportHold;

    @Column(nullable = false, name = "status", columnDefinition = "TINYINT", length = 1)
    private Boolean status;

    @JsonManagedReference
    @ToString.Exclude
    @OneToMany(mappedBy = "productEntity", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ServiceDataSet> serviceDataset;

    @JsonManagedReference
    @ToString.Exclude
    @OneToMany(mappedBy = "productEntity",fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AccountProduct> accountProducts;

    @JsonManagedReference
    @ToString.Exclude
    @OneToMany(mappedBy = "productEntity",fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CardProduct> cardProducts;

    @JsonManagedReference
    @ToString.Exclude
    @OneToMany(mappedBy = "productEntity", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductServiceEntity> productServiceEntities;

	@Override
	public String getId() {
		return code;
	}

}
