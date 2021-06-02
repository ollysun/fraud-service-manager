package com.etz.fraudeagleeyemanager.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "product")
@SQLDelete(sql = "UPDATE product SET deleted = true WHERE code = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted = false")
@Getter
@Setter
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

    @Column( name = "support_hold", columnDefinition = "TINYINT default 0", length = 1)
    private Boolean supportHold;

    @Column(nullable = false, name = "status", columnDefinition = "TINYINT", length = 1)
    private Boolean status;

    @JsonManagedReference
    @ToString.Exclude
    @OneToMany(mappedBy = "productEntity", fetch = FetchType.EAGER,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductDataSet> productDataset;

    @JsonManagedReference
    @ToString.Exclude
    @OneToMany(mappedBy = "productEntity", fetch = FetchType.EAGER,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductRule> productRules;


    @ToString.Exclude
    @ManyToMany(mappedBy = "products", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private Set<Account> account;

    @ToString.Exclude
    @ManyToMany(mappedBy = "products", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private Set<Card> cards;
}
