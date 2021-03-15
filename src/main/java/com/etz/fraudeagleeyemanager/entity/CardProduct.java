package com.etz.fraudeagleeyemanager.entity;

import com.etz.fraudeagleeyemanager.constant.Status;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "card_product")
public class CardProduct {

    @EmbeddedId
    private CardProductKey Id;

    @ManyToOne
    @MapsId("code")
    @JoinColumn(name = "code")
    private Product product;

    @ManyToOne
    @MapsId("cardId")
    @JoinColumn(name = "card_id")
    private Card card;

    @Column(nullable = false, name = "status", columnDefinition = "TINYINT", length = 1)
    @Enumerated(EnumType.ORDINAL)
    private Status status;

    @Column(nullable = false,name = "created_at")
    private LocalDateTime createdAt;

}
