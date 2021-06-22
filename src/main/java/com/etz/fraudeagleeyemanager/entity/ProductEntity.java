package com.etz.fraudeagleeyemanager.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "product")
@SQLDelete(sql = "UPDATE product SET deleted = true WHERE code = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted = false")
@Getter
@Setter
public class ProductEntity extends BaseAuditVersionEntity<String> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
    @Column(name = "code", unique=true,columnDefinition="VARCHAR(100)")
    private String code;

    @Column(nullable = false, name = "name", unique = true, length = 200)
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
    private List<ProductDataSet> productDataset;

    @JsonManagedReference
    @ToString.Exclude
    @OneToMany(mappedBy = "productEntity", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductRule> productRules;

    @JsonManagedReference
    @OneToMany(mappedBy = "productEntity",fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AccountProduct> accountProducts;

    @JsonManagedReference
    @OneToMany(mappedBy = "productEntity",fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CardProduct> cardProducts;

	@Override
	public String getId() {
		return code;
	}
}
