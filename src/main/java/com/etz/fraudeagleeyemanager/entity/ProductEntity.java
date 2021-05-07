package com.etz.fraudeagleeyemanager.entity;

import com.etz.fraudeagleeyemanager.constant.Status;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "product")
@EqualsAndHashCode(callSuper = true)
@Data
public class ProductEntity extends BaseEntity implements Serializable {

    @Id
    @Column(name = "code", unique=true,columnDefinition="VARCHAR(100)")
    private String code;

    @NotBlank(message = "ProductEntity Name cannot be empty")
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

    @Column(nullable = false, name = "support_hold", columnDefinition = "TINYINT default false", length = 1)
    private Status supportHold;

    @Column(nullable = false, name = "status", columnDefinition = "TINYINT", length = 1)
    private Status status;


    @OneToMany(mappedBy = "productEntity",
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductDataSet> productDataset;

    @ToString.Exclude
    @OneToMany(mappedBy = "productEntity", fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE)
    private Set<ProductRule> productRules;


    @OneToMany(mappedBy = "productEntity", cascade = CascadeType.REMOVE)
    private Set<CardProduct> products;

    @OneToMany(mappedBy = "productEntity", cascade = CascadeType.REMOVE)
    private Set<AccountProduct> productLists;
}
