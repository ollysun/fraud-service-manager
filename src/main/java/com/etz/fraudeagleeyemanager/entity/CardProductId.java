package com.etz.fraudeagleeyemanager.entity;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class CardProductId implements Serializable {

    @NotNull
    private String productCode;

    @NotNull
    private Long cardId;
}
