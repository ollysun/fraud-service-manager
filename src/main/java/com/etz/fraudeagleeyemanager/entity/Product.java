package com.etz.fraudeagleeyemanager.entity;

import com.etz.fraudeagleeyemanager.constant.BooleanStatus;
import com.etz.fraudeagleeyemanager.constant.Status;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "product", uniqueConstraints = @UniqueConstraint(
        columnNames = {"code", "name"}))
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@IdClass(ProductId.class)
public class Product extends BaseEntity implements Serializable {

    @Id
    @NotBlank(message = "Product Code cannot be empty")
    @Column(name = "code")
    private String code;

    @NotBlank(message = "Product Name cannot be empty")
    @Column(nullable = false, name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(nullable = false, name = "use_card", columnDefinition = "TINYINT", length = 1)
    @Enumerated(EnumType.ORDINAL)
    private BooleanStatus useCard;

    @Column(nullable = false,name = "use_account", columnDefinition = "TINYINT", length = 1)
    @Enumerated(EnumType.ORDINAL)
    private BooleanStatus useAccount;

    @Column(name = "callback_url")
    private String callbackURL;

    @Column(nullable = false, name = "status", columnDefinition = "TINYINT", length = 1)
    @Enumerated(EnumType.ORDINAL)
    private Status status;

    @ToString.Exclude
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    Set<CardProduct> cardProducts = new HashSet<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    Set<AccountProduct> accountProducts = new HashSet<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    Set<ProductDataset> productDataset = new HashSet<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<ProductRule> productRules = new HashSet<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    Set<TransactionLog> transactionLog = new HashSet<>();





}
