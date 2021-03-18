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
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "product", uniqueConstraints = @UniqueConstraint(
        columnNames = {"code", "name"}))
@Getter
@Setter
@RequiredArgsConstructor
@IdClass(ProductId.class)
public class Product implements Serializable {

    @Id
    private Long id;

    @Id
    private String code;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<ProductRule> productRules;

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

    @NotBlank(message = "Please enter the name of the creator")
    @Column(nullable = false,name = "created_by")
    private String createdBy;

    @Column(nullable = false,name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ToString.Exclude
    @OneToMany(mappedBy = "product")
    Set<CardProduct> cardProduct = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product that = (Product) o;

        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return 335418294;
    }

    @Override
    public String toString() {
        return "Product(" +
                "id = " + id + ", " +
                "code = " + code + ", " +
                "name = " + name + ", " +
                "description = " + description + ", " +
                "useCard = " + useCard + ", " +
                "useAccount = " + useAccount + ", " +
                "callbackURL = " + callbackURL + ", " +
                "status = " + status + ", " +
                "createdBy = " + createdBy + ", " +
                "createdAt = " + createdAt + ", " +
                "updatedBy = " + updatedBy + ", " +
                "updatedAt = " + updatedAt + ")";
    }
}
