package com.etz.fraudeagleeyemanager.entity;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CardProductId implements Serializable {
    private Long id;
    private String productCode;

    private Long cardId;
}
