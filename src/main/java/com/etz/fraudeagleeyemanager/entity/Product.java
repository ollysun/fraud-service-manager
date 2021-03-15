package com.etz.fraudeagleeyemanager.entity;

import com.etz.fraudeagleeyemanager.constant.Status;
import com.etz.fraudeagleeyemanager.constant.UseStatus;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.io.*;
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
public class Product implements Serializable {

    @Id
    private Long id;

    @Id
    private String code;

    @NotBlank(message = "Product Name cannot be empty")
    @Column(nullable = false, name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(nullable = false, name = "use_card", columnDefinition = "TINYINT", length = 1)
    @Enumerated(EnumType.ORDINAL)
    private UseStatus useCard;

    @Column(nullable = false,name = "use_account", columnDefinition = "TINYINT", length = 1)
    @Enumerated(EnumType.ORDINAL)
    private UseStatus useAccount;

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
}
