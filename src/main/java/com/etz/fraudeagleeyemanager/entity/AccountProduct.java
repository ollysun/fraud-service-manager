package com.etz.fraudeagleeyemanager.entity;

import com.etz.fraudeagleeyemanager.constant.Status;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@IdClass(AccountProductKey.class)
@Table(name = "account_product")
public class AccountProduct {

    @Id
    private String productCode;

    @Id
    private Long accountId;

    @ManyToOne
    @MapsId("productCode")
    @JoinColumn(name = "product_code",  referencedColumnName="code")
    private Product product;

    @ManyToOne
    @MapsId("accountId")
    @JoinColumn(name = "account_id", referencedColumnName="id")
    private Account account;

    @Column(nullable = false, name = "status", columnDefinition = "TINYINT", length = 1)
    @Enumerated(EnumType.ORDINAL)
    private Status status;

    @Column(nullable = false,name = "created_at")
    private LocalDateTime createdAt;

}
