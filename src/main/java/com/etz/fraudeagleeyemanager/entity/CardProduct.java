package com.etz.fraudeagleeyemanager.entity;

import com.etz.fraudeagleeyemanager.constant.Status;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@IdClass(CardProductKey.class)
@Table(name = "card_product")
public class CardProduct implements Serializable {


    @Id
    private String productCode;

    @Id
    private Long cardId;

    @ManyToOne
    @MapsId("productCode")
    @JoinColumn(name = "product_code", referencedColumnName="code")
    private Product product;

    @ManyToOne
    @MapsId("cardId")
    @JoinColumn(name = "card_id", referencedColumnName="id")
    private Card card;

    @Column(nullable = false, name = "status", columnDefinition = "TINYINT", length = 1)
    @Enumerated(EnumType.ORDINAL)
    private Status status;

    @Column(nullable = false,name = "created_at")
    private LocalDateTime createdAt;

}
